//Initialize the websocket and other ptaskman related js const/vars
var relativeRootWsProto=document.location.href.replace(/^http\:\/\//ig,'ws://').replace(/ptaskman\.jsp$/,'')+'../',
    ws = new WebSocket(relativeRootWsProto+"ptoperations"), //Opening the websocket firsttime. On the same host, same port, same app, different proto
    tConfig = function(theData) { //We build a default table config object with this func
        return {
            data: theData,
            columns: [
                { title: "PID" },
                { title: "Name" },
                { title: "Owner" },
                { title: "CPU usage (%)" },
                { title: "RAM usage (Mb)" }
            ],
            stateSave: true,
            pageLength: 9999,
            scrollY:        "400px",
            scrollX:        true,
            scrollCollapse: true,
            bPaginate: false,
            bLengthChange: false,
            bFilter: true,
            bInfo: false,
            bAutoWidth: false
        };
    },
    isTableUpdating=false, //We use this as a flag for something like a Thread synchronization
    $theDataTable=null, //Should be a global ref to the DataTable
    globalcpuinfoSmoothie=null,
    globalmeminfoSmoothie=null,
    isGlobalmeminfoSmoothieInit=false, //Flag for the RAM chart defining whether it is already initialized
    processcpuinfoSmoothie=null,
    processmeminfoSmoothie=null,
    processcpuCanvasHTML='<canvas id="processcpuinfo" style="width:100%; height:112px"></canvas>', //These canvases are created dynamically on the DOM
    processmemCanvasHTML='<canvas id="processmeminfo" style="width:100%; height:112px"></canvas>',
    _selectedPidRow=null,
    _selectedPid=-1;

function selectedPidRow() {
    this.pid=null;
    this.name=null;
    this.cpuusage=null;
    this.memusage=null;
    this._cpuSmoothie=null;
    this._memSmoothie=null;
    this._$pCont=null;
    this._$mCont=null;

    this.refreshData=function($selRow) {
        this.pid=$selRow.find('td').eq(0).text();
        this.name=$selRow.find('td').eq(1).text();
        this.cpuusage=$selRow.find('td').eq(3).text();
        this.memusage=$selRow.find('td').eq(4).text();
    }

    this.createGUI=function() {
        $('#procinfo_gui').css('display','block');
        this._$pCont=$('#processcpuinfo_cont');
        this._$mCont=$('#processmeminfo_cont');
        this._$pCont.html(processcpuCanvasHTML);
        this._$mCont.html(processmemCanvasHTML);

        //CPU Smoothie chart init (for a process)
        this._cpuSmoothie=new pSmoothie({
            maxValue:100,
            minValue:0
        },
        [
            {
                strokeStyle: 'rgb(0, 255, 0)',
                fillStyle: 'rgba(0, 255, 0, 0.4)',
                lineWidth: 3
            }
        ],"processcpuinfo");

        //RAM Smoothie chart init (for a process)
        this._memSmoothie=new pSmoothie({

        },
        [
            {
                strokeStyle: 'rgb(0, 255, 0)',
                fillStyle: 'rgba(0, 255, 0, 0.4)',
                lineWidth: 3
            }
        ],"processmeminfo");
    }

    this.updateChartsAndSpots=function() {
        //Update the placeholders
        $('#procname').html(this.name);
        $('#procpid').html(this.pid);
        $('#proccpuinfo').html(this.cpuusage);
        $('#procraminfo').html(this.memusage);

        //Update the charts
        if (this._cpuSmoothie && this._cpuSmoothie.isReady) {
            this._cpuSmoothie.lines[0].append(new Date().getTime(), this.cpuusage);
        }
        if (this._memSmoothie && this._memSmoothie.isReady) {
            this._memSmoothie.lines[0].append(new Date().getTime(), this.memusage);
        }
    }

    this.destroyGUI=function() {
        if (this._$pCont) this._$pCont.html('');
        if (this._$mCont) this._$mCont.html('');
        $('#procinfo_gui').css('display','none');
    }

    return this;
}

function showProcessInfoUI($selRow) {
    _selectedPidRow=new selectedPidRow();
    _selectedPidRow.refreshData($selRow);
    _selectedPidRow.createGUI();
    _selectedPidRow.updateChartsAndSpots();
    _selectedPid=_selectedPidRow.pid;
}

function hideProcessInfoUI() {
    _selectedPidRow.destroyGUI();
    _selectedPidRow=null;
    _selectedPid=-1;
}

//Wrapper type around the SmoothieChart type
//It has only the .lines array intended for public access once an instance is created.
//Convenient for multiple charts creation and update on the same page
function pSmoothie(addConf, linesStyles, canvasId) {
    this._canvasId=canvasId;
    this._linesStyles=linesStyles;
    this._numLines=linesStyles.length;
    this.lines=[]; //o.lines is intended for public access. It is an array with the lines which are supposed to be updated by the server thru the socket
    this.isReady=false; //o.isReady is intended for public access. It is a flag which designates when the Chart is initialized and the line(s) are ready to receive data

    //Default configuration
    var smDefConf={
        grid: {
            lineWidth: 1,
            millisPerLine: 250,
            verticalSections: 6,
            responsive: true
        },
        labels: {
            fillStyle: 'rgb(60, 0, 0)'
        }
    };

    //Adding/replacing config properties with the passed argument one (if any)
    for (var i in addConf) {
        smDefConf[i]=addConf[i];
    }

    this._smoothieO = new SmoothieChart(smDefConf);
    this._smoothieO.streamTo(document.getElementById(this._canvasId));

    // Data
    for (var i=0; i<this._numLines; i++) {
        this.lines[i] = new TimeSeries();
    }

    // Add to SmoothieChart
    for (var i=0; i<this._numLines; i++) {
        this._smoothieO.addTimeSeries(this.lines[i], this._linesStyles[i]);
    }

    this._smoothieO.streamTo(document.getElementById(this._canvasId), 1000);

    this.isReady=true;
    return this;
}

ws.onopen = function(){
};

$(document).ready(function() {
    //First time initialize the datatable with empty data
    $theDataTable=$('#processtable').DataTable( tConfig([]) );

    //CPU Smoothie chart init
    globalcpuinfoSmoothie=new pSmoothie({
        maxValue:100,
        minValue:0
    },
    [
        {
            strokeStyle: 'rgb(0, 255, 0)',
            fillStyle: 'rgba(0, 255, 0, 0.4)',
            lineWidth: 3
        }
    ],"globalcpuinfo");
} );

//Websocket receive control
ws.onmessage = function(message) {
    // console.log(message.data);
    var aJson=JSON.parse(message.data);


    if (aJson.cmd=="procList") { //If this is a table update command received from the server ####################################
        isTableUpdating=true; //Synchronizing


        var selPid=null,
            $selRow=$theDataTable.$('tr.selected');

        //Remember the pid of the selected row (if any)
        if ($selRow.length) {
            selPid = $selRow.find('td').eq(0).text();

            //Remember the entire selected row with its data structure
            if (_selectedPid!==selPid) {
                if (_selectedPid !== -1 && _selectedPidRow !== null) {
                    hideProcessInfoUI(); //Another row has been selected. Different than the last one
                }

                showProcessInfoUI($selRow);
            }
            else { //Still the same pid is selected, just refreshing its data, spots and charts
                _selectedPidRow.refreshData($selRow);
                _selectedPidRow.updateChartsAndSpots();
            }
        }
        else { //There is no selected row, so invalidating them
            if (_selectedPid !== -1) hideProcessInfoUI();
        }

        //Recreate the datatable grid
        $theDataTable.destroy();              // remove table enhancements
        $('#processtable').empty();          // empty the table content ( this remove the rows)
        $theDataTable = $('#processtable').DataTable(tConfig(aJson.cmdData));   // recreate the table with the new configuration

        $('#processtable tbody').on( 'click', 'tr', function () {
            var $this=$(this);
            if ( $this.hasClass('selected') ) { //the same row is clicked, just unselecting
                $this.removeClass('selected');
            }
            else {
                $r=$theDataTable.$('tr.selected'); //New row is clicked. Unselecting the old one (if any) and selecting the new one
                $r.removeClass('selected');
                $this.addClass('selected');
                if (_selectedPid !== -1) hideProcessInfoUI();
                showProcessInfoUI($this);
            }
        } );

        //Restoring the selected row (if any)
        //Using native table colums search due to performance concerns
        if (selPid) {
            var tbody=$('#processtable tbody').get(0),
                chlds=tbody.childNodes,
                chldsLen=chlds.length,
                searchedColum=0, //Means: the first column
                $wantedRow=null;

            for (var i=0; i<chldsLen; i++) { //Skip the first row
                if (selPid == chlds[i].childNodes[searchedColum].innerText) {
                    $wantedRow = $(chlds[i]);
                    break;
                }
            }

            if ($wantedRow!==null) {
                $wantedRow.addClass('selected');
            }
        }


        isTableUpdating=false;
    }
    //####################################################################################################################
    // <-----------------------------------------------------------
    else if (aJson.cmd=="globRes") { //If this is a global resource update received from the server #####################
        var cmdData=aJson.cmdData;
        $('#osname').html(cmdData.osname);
        $('#cpucores').html(cmdData.cpucores);
        $('#cpufreq').html(cmdData.cpufreq+"MHz");
        $('#uptime').html(cmdData.uptime);
        $('#cpuinfo').html(cmdData.cpuinfo);
        $('#raminfo').html(cmdData.raminfo);

        if (globalcpuinfoSmoothie.isReady) {
            globalcpuinfoSmoothie.lines[0].append(new Date().getTime(), cmdData.cpuinfo);
        }

        initGlobalmeminfoSmoothie(cmdData.ramtot); //We initialize the chart for the global RAM usage here because we need to know its max value before initializing it
        if (globalmeminfoSmoothie && globalmeminfoSmoothie.isReady) {
            globalmeminfoSmoothie.lines[0].append(new Date().getTime(), cmdData.ramused);
        }

    }
    //####################################################################################################################
    // <-----------------------------------------------------------
};

//Websocket send control
/*function postToServer(){
    ws.send(document.getElementById("msg").value);
    document.getElementById("msg").value = "";
}*/

//Websocket shutdown
function closeConnect(){
    ws.close();
}

//Conditionally initializes the global resource RAM chart
function initGlobalmeminfoSmoothie(max) {
    if (!isGlobalmeminfoSmoothieInit && document.getElementById("globalmeminfo")) {
        globalmeminfoSmoothie=new pSmoothie({
            maxValue:max,
            minValue:0
        },[
            /*{
                strokeStyle: 'rgb(0, 255, 0)',
                fillStyle: 'rgba(0, 255, 0, 0.4)',
                lineWidth: 1
            },*/
            {
                strokeStyle:'rgb(255, 0, 255)',
                fillStyle:'rgba(255, 0, 255, 0.3)',
                lineWidth:1
            }
        ],"globalmeminfo");
        isGlobalmeminfoSmoothieInit=true;
    }
}

//Invokes a kill process command to the server for the selected PID
function killproc() {
    if (_selectedPid !== -1) {
        ws.send("kill_process "+_selectedPid);
    }
}
