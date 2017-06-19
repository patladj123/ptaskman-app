<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/ptaskman.css"/>
    	<link rel="stylesheet" type="text/css" href="../css/jquery.dataTables.css"/>
		 
		<script type="text/javascript" src="../js/jquery-2.2.4.min.js"></script>
		<script type="text/javascript" src="../js/jquery.dataTables.js"></script>
		<script type="text/javascript" src="../js/smoothie.js"></script>
		<script type="text/javascript" src="../js/ptaskman.js"></script>
    </head>

    <body>
        <div class="fill-width" style="height: 104px;">
            <div class="box1" style="border: 1px solid #2b542c; background-color: #2b542c;"><div class="center-ver-any">
                <div class="versep" style="height:30px;"></div>
                <div class="bold" style="color: white;">CPU usage&nbsp(<span id="cpuinfo" class="bold"></span>%)</div>
                <canvas id="globalcpuinfo" style="width:100%; height:112px"></canvas>
            </div></div>
            <div class="box2" style="border: 1px solid orangered;"><div class="center-ver-any">
                <div style="font-weight: bold; font-style: italic;">&#11013&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Global resources&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#10145</div>
                <span>OS name: </span><span id="osname" class="bold"></span>
                <div class="versep"></div>
                <span>CPU cores: </span><span id="cpucores" class="bold"></span>,&nbsp;<span>CPU freq: </span><span id="cpufreq" class="bold"></span>
                <div class="versep"></div>
                <span>Uptime: </span><span id="uptime" class="bold"></span>
            </div></div>
            <div class="box3" style="border: 1px solid darkred; background-color: darkred;"><div class="center-ver-any">
                <div class="versep" style="height:30px;"></div>
                <div class="bold" style="color: white;">RAM usage&nbsp(<span id="raminfo" class="bold"></span> in use)</div>
                <canvas id="globalmeminfo" style="width:100%; height:112px"></canvas>
            </div></div>
            <span class="stretch"></span>
        </div>
        <div class="versep" style="height: 22px;"></div>

        <div class="fill-width" style="height: 472px;">
            <table id="processtable" class="display" width="100%"></table>
        </div>

        <div class="fill-width" style="height: 104px; display: none;" id="procinfo_gui">
            <div class="box1" style="border: 1px solid #4cae4c; background-color:#4cae4c;"><div class="center-ver-any">
                <div class="versep" style="height:30px;"></div>
                <div class="bold" style="color: white;">Process CPU usage&nbsp(<span id="proccpuinfo" class="bold"></span>%)</div>
                <div id="processcpuinfo_cont" style="width:100%; height:112px;"></div>
            </div></div>
            <div class="box2" style="border: 1px solid #2b2b2b;"><div class="center-ver-any">
                <div style="font-weight: bold; font-style: italic;">&#11013&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="procname">?</span> resources&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#10145</div>
                <span>PID: </span><span id="procpid" class="bold">?</span>
                <div class="versep" style="height:1px;"></div>
                <form style="margin:0;">
                    <input type="button" name="kill_proc" value="Kill this process" onclick="killproc();">
                </form>
            </div></div>
            <div class="box3" style="border: 1px solid #c0a16b; background-color:#c0a16b"><div class="center-ver-any">
                <div class="versep" style="height:30px;"></div>
                <div class="bold" style="color: white;">Process RAM usage&nbsp(<span id="procraminfo" class="bold"></span>Mb)</div>
                <div id="processmeminfo_cont" style="width:100%; height:112px;"></div>
            </div></div>
            <span class="stretch"></span>
        </div>


    </body>
</html>