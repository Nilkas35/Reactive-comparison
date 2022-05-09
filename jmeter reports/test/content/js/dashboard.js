/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 99.99962791793888, "KoPercent": 3.720820611185052E-4};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.9999320058260915, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.9999113305465358, 500, 1500, "Get Drink 1"], "isController": false}, {"data": [0.9999138890390971, 500, 1500, "Get Cookie 1"], "isController": false}, {"data": [0.9996398341217339, 500, 1500, "Get Member 1"], "isController": false}, {"data": [0.9996574996350406, 500, 1500, "Get Member 2"], "isController": false}, {"data": [0.9999996904402373, 500, 1500, "Get Fruit 2"], "isController": false}, {"data": [1.0, 500, 1500, "Get Cart 2"], "isController": false}, {"data": [1.0, 500, 1500, "Get Cart 1"], "isController": false}, {"data": [0.9996639679793523, 500, 1500, "Get Cart R 2"], "isController": false}, {"data": [0.9999024765043569, 500, 1500, "Get Cookie 2"], "isController": false}, {"data": [1.0, 500, 1500, "Get Employee 2"], "isController": false}, {"data": [1.0, 500, 1500, "Get Fruit 1"], "isController": false}, {"data": [1.0, 500, 1500, "Get Employee 1"], "isController": false}, {"data": [0.999644969080376, 500, 1500, "Get Cart R 1"], "isController": false}, {"data": [1.0, 500, 1500, "Get Candy 1"], "isController": false}, {"data": [1.0, 500, 1500, "Get Candy 2"], "isController": false}, {"data": [0.9999066789278295, 500, 1500, "Get Drink 2"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 39238656, 146, 3.720820611185052E-4, 29.139163634955167, 0, 50191, 49.0, 56.0, 65.0, 72.0, 18814.842945343025, 1949.0403225598604, 2328.3563871116007], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Get Drink 1", 2199179, 21, 9.549018065378034E-4, 33.93952470444905, 0, 50050, 22.0, 60.0, 88.0, 195.0, 6948.301138051096, 624.4242679246207, 861.7439630390104], "isController": false}, {"data": ["Get Cookie 1", 2200649, 21, 9.542639466811836E-4, 33.921190067114615, 0, 50072, 22.0, 61.0, 88.95000000000073, 190.9900000000016, 6956.242334585088, 625.1373304520856, 869.5219942090556], "isController": false}, {"data": ["Get Member 1", 1336884, 13, 9.724104709159508E-4, 55.95086709093857, 0, 50169, 51.0, 54.0, 56.0, 71.0, 4446.586441557405, 399.6047470680583, 551.475572964479], "isController": false}, {"data": ["Get Member 2", 1335765, 15, 0.0011229520162603453, 55.997385767705595, 1, 50191, 51.0, 55.0, 57.0, 71.0, 4444.845600958339, 399.4648208729868, 551.2588401458306], "isController": false}, {"data": ["Get Fruit 2", 4845591, 0, 0.0, 12.657235825310122, 0, 1053, 9.0, 23.0, 31.0, 54.0, 16132.772001318433, 1449.4287344934526, 2000.8418400072662], "isController": false}, {"data": ["Get Cart 2", 1358945, 0, 0.0, 55.05551659559386, 0, 332, 45.0, 77.0, 95.0, 135.0, 4523.679541422137, 855.7543293608754, 552.2069752712571], "isController": false}, {"data": ["Get Cart 1", 1353645, 0, 0.0, 55.25984582368392, 0, 329, 47.0, 86.0, 101.0, 142.0, 4506.441840335575, 852.494229964961, 550.1027637128387], "isController": false}, {"data": ["Get Cart R 2", 1342134, 17, 0.0012666395456787474, 55.7351009660734, 0, 50178, 50.0, 54.0, 57.0, 71.0, 4470.337839863305, 401.77170349664095, 545.6886250972168], "isController": false}, {"data": ["Get Cookie 2", 2194343, 24, 0.0010937214464648416, 34.015507602958415, 0, 50050, 22.0, 61.0, 87.0, 190.0, 6936.988856397692, 623.4314367146131, 867.1141231328539], "isController": false}, {"data": ["Get Employee 2", 1356329, 0, 0.0, 55.14321230321284, 0, 364, 48.0, 85.0, 100.0, 141.0, 4516.790659571208, 854.4517150160597, 569.0097608248886], "isController": false}, {"data": ["Get Fruit 1", 4845280, 0, 0.0, 12.648338176535459, 0, 298, 10.0, 24.0, 32.0, 55.9900000000016, 16153.248632303963, 1451.2684318085592, 2003.381422170511], "isController": false}, {"data": ["Get Employee 1", 1357999, 0, 0.0, 55.068602406923674, 0, 324, 47.0, 85.0, 100.0, 140.0, 4522.1713098322325, 855.4691662531885, 569.6875966487872], "isController": false}, {"data": ["Get Cart R 1", 1330870, 18, 0.0013524987414247822, 56.20630715246408, 1, 50178, 51.0, 54.0, 57.0, 72.0, 4430.178755700543, 398.17179542125757, 540.7859909165807], "isController": false}, {"data": ["Get Candy 1", 4903278, 0, 0.0, 12.570736964128448, 0, 288, 5.0, 8.0, 10.0, 14.0, 16162.219533982245, 1452.0744112562174, 1988.7106067204716], "isController": false}, {"data": ["Get Candy 2", 5075690, 0, 0.0, 12.243496352220749, 0, 299, 1.0, 1.0, 1.0, 2.0, 16829.992108386992, 1512.069603487894, 2070.877935211681], "isController": false}, {"data": ["Get Drink 2", 2202075, 17, 7.719991371774349E-4, 33.90082490378167, 0, 50050, 22.0, 61.0, 87.0, 200.9900000000016, 6958.110567625981, 625.2743963009833, 862.9621289904289], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 9, 6.164383561643835, 2.29365654114147E-5], "isController": false}, {"data": ["Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 137, 93.83561643835617, 3.491454957070905E-4], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 39238656, 146, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 137, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 9, "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["Get Drink 1", 2199179, 21, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 19, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 2, "", "", "", "", "", ""], "isController": false}, {"data": ["Get Cookie 1", 2200649, 21, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 18, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 3, "", "", "", "", "", ""], "isController": false}, {"data": ["Get Member 1", 1336884, 13, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 13, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Get Member 2", 1335765, 15, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 15, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["Get Cart R 2", 1342134, 17, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 17, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Get Cookie 2", 2194343, 24, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 22, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 2, "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["Get Cart R 1", 1330870, 18, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 18, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["Get Drink 2", 2202075, 17, "Non HTTP response code: java.net.SocketTimeoutException/Non HTTP response message: Read timed out", 15, "Non HTTP response code: java.net.SocketException/Non HTTP response message: Socket closed", 2, "", "", "", "", "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
