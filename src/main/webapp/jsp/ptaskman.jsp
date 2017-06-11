<html>
    <head>
    
    	<link rel="stylesheet" type="text/css" href="../css/jquery.dataTables.min.css"/>
		<link rel="stylesheet" type="text/css" href="../css/responsive.dataTables.min.css"/>
		<link rel="stylesheet" type="text/css" href="../css/select.dataTables.min.css"/>
		 
		<script type="text/javascript" src="../js/jquery-2.2.4.min.js"></script>
		<script type="text/javascript" src="../js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="../js/dataTables.responsive.min.js"></script>
		<script type="text/javascript" src="../js/dataTables.select.min.js"></script>
		<script type="text/javascript" src="../js/smoothie.js"></script>
		<script type="text/javascript" src="../js/ptaskman.js"></script>
    </head>

    <body>          
        <textarea id="chatlog" readonly></textarea><br/>
        <input id="msg" type="text" />
        <button type="submit" id="sendButton" onClick="postToServer()">Send!</button>
        <button type="submit" id="sendButton" onClick="closeConnect()">End</button><br><br>
        
        <table id="processtable" class="display" width="100%"></table>
        
        <script>
        var dataSet = [
            [ "Tiger Nixon", "System Architect", "Edinburgh", "5421", "2011/04/25", "$320,800" ],
            [ "Garrett Winters", "Accountant", "Tokyo", "8422", "2011/07/25", "$170,750" ],
            [ "Ashton Cox", "Junior Technical Author", "San Francisco", "1562", "2009/01/12", "$86,000" ],
            [ "Cedric Kelly", "Senior Javascript Developer", "Edinburgh", "6224", "2012/03/29", "$433,060" ],
            [ "Airi Satou", "Accountant", "Tokyo", "5407", "2008/11/28", "$162,700" ],
            [ "Brielle Williamson", "Integration Specialist", "New York", "4804", "2012/12/02", "$372,000" ],
            [ "Herrod Chandler", "Sales Assistant", "San Francisco", "9608", "2012/08/06", "$137,500" ],
            [ "Rhona Davidson", "Integration Specialist", "Tokyo", "6200", "2010/10/14", "$327,900" ],
            [ "Colleen Hurst", "Javascript Developer", "San Francisco", "2360", "2009/09/15", "$205,500" ],
            [ "Sonya Frost", "Software Engineer", "Edinburgh", "1667", "2008/12/13", "$103,600" ],
            [ "Jena Gaines", "Office Manager", "London", "3814", "2008/12/19", "$90,560" ],
            [ "Quinn Flynn", "Support Lead", "Edinburgh", "9497", "2013/03/03", "$342,000" ],
            [ "Charde Marshall", "Regional Director", "San Francisco", "6741", "2008/10/16", "$470,600" ],
            [ "Haley Kennedy", "Senior Marketing Designer", "London", "3597", "2012/12/18", "$313,500" ],
            [ "Tatyana Fitzpatrick", "Regional Director", "London", "1965", "2010/03/17", "$385,750" ],
            [ "Michael Silva", "Marketing Designer", "London", "1581", "2012/11/27", "$198,500" ],
            [ "Paul Byrd", "Chief Financial Officer (CFO)", "New York", "3059", "2010/06/09", "$725,000" ],
            [ "Gloria Little", "Systems Administrator", "New York", "1721", "2009/04/10", "$237,500" ],
            [ "Bradley Greer", "Software Engineer", "London", "2558", "2012/10/13", "$132,000" ],
            [ "Dai Rios", "Personnel Lead", "Edinburgh", "2290", "2012/09/26", "$217,500" ],
            [ "Jenette Caldwell", "Development Lead", "New York", "1937", "2011/09/03", "$345,000" ],
            [ "Yuri Berry", "Chief Marketing Officer (CMO)", "New York", "6154", "2009/06/25", "$675,000" ],
            [ "Caesar Vance", "Pre-Sales Support", "New York", "8330", "2011/12/12", "$106,450" ],
            [ "Doris Wilder", "Sales Assistant", "Sidney", "3023", "2010/09/20", "$85,600" ],
            [ "Angelica Ramos", "Chief Executive Officer (CEO)", "London", "5797", "2009/10/09", "$1,200,000" ],
            [ "Gavin Joyce", "Developer", "Edinburgh", "8822", "2010/12/22", "$92,575" ],
            [ "Jennifer Chang", "Regional Director", "Singapore", "9239", "2010/11/14", "$357,650" ],
            [ "Brenden Wagner", "Software Engineer", "San Francisco", "1314", "2011/06/07", "$206,850" ],
            [ "Fiona Green", "Chief Operating Officer (COO)", "San Francisco", "2947", "2010/03/11", "$850,000" ],
            [ "Shou Itou", "Regional Marketing", "Tokyo", "8899", "2011/08/14", "$163,000" ],
            [ "Michelle House", "Integration Specialist", "Sidney", "2769", "2011/06/02", "$95,400" ],
            [ "Suki Burks", "Developer", "London", "6832", "2009/10/22", "$114,500" ],
            [ "Prescott Bartlett", "Technical Author", "London", "3606", "2011/05/07", "$145,000" ],
            [ "Gavin Cortez", "Team Leader", "San Francisco", "2860", "2008/10/26", "$235,500" ],
            [ "Martena Mccray", "Post-Sales support", "Edinburgh", "8240", "2011/03/09", "$324,050" ],
            [ "Unity Butler", "Marketing Designer", "San Francisco", "5384", "2009/12/09", "$85,675" ]
        ];
         
        $(document).ready(function() {
            $('#processtable').DataTable( {
                data: dataSet,
                columns: [
                    { title: "PID" },
                    { title: "Name" },
                    { title: "Owner" },
                    { title: "CPU usage" },
                    { title: "Mem usage" }
                ]
            } );
        } );
        </script>
        <br><Br>
        
        
        <canvas id="mycanvas" width="400" height="100"></canvas>
        
        <script>
        var smoothie = new SmoothieChart({
        	  grid: { strokeStyle:'rgb(125, 0, 0)', fillStyle:'rgb(60, 0, 0)',
                  lineWidth: 1, millisPerLine: 250, verticalSections: 6, },
          labels: { fillStyle:'rgb(60, 0, 0)' }
        });
        smoothie.streamTo(document.getElementById("mycanvas"));
        
     	// Data
        var line1 = new TimeSeries();
        var line2 = new TimeSeries();

        // Add a random value to each line every second
        setInterval(function() {
          line1.append(new Date().getTime(), Math.random());
          line2.append(new Date().getTime(), Math.random());
        }, 1000);

        // Add to SmoothieChart
        smoothie.addTimeSeries(line1,
        		  { strokeStyle:'rgb(0, 255, 0)', fillStyle:'rgba(0, 255, 0, 0.4)', lineWidth:3 });
        smoothie.addTimeSeries(line2,
        		  { strokeStyle:'rgb(255, 0, 255)', fillStyle:'rgba(255, 0, 255, 0.3)', lineWidth:3 });
        
        
        smoothie.streamTo(document.getElementById("mycanvas"), 1000 /*delay*/); 
        
        
        
        
        
        </script>
    </body>
</html>