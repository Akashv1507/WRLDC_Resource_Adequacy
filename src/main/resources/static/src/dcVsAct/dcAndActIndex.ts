import {getDcAndActData, getDcData} from "../fetchDataApi"
import { IdcDataObj } from "../interfacesTypes/dcDataTypes";

window.onload = async () => {
	let intervalId = null;
	intervalId = setInterval(refreshData, 1000 * 60 * 5);
	(document.getElementById('dcVsActBtn') as HTMLButtonElement).onclick = refreshData;
	refreshData()
}
const refreshData = async () => {
	
	const stateName = (document.getElementById("stateName") as HTMLSelectElement).value;
	const metInfoDiv = document.getElementById("metaInfo") as HTMLDivElement;
	
	// removing table rows and destroying datatable before next refresh cycle
	$(`#dcAndAct_tbl`).DataTable().destroy();
	$(`#dcAndAct_tbl tbody`).empty();
	//fetch DC And Act data and push data to datatble div 
	let dcAndActData = await getDcAndActData(stateName);
	
    let dcAndActTbl =$(`#dcAndAct_tbl`).DataTable({
      dom: "frtip",
      data: dcAndActData.data,
      
      //order: [[1, "desc"]],
      columns: [
        { data: "plantName", title: "Plant_Name" },
        { data: "dateTime", title: "DateTime" },
        { data: "dcValue", title: "DCVal" },
        { data: "actualValue", title: "ActVal" },
      ],
    });
    metInfoDiv.innerHTML= `Showing Data For ${stateName}`;
    
	(dcAndActTbl as any).on('click', 'tbody tr', async function () {
		// removing table rows and destroying datatable before next refresh cycle
	$(`#dc_tbl`).DataTable().destroy();
	$(`#dc_tbl tbody`).empty();
    const data = dcAndActTbl.row(this).data();
    let plantName=data['plantName']
    //fetch DC data of current clicked generator and push data to datatble div 
	let dcAData = await getDcData(plantName);
	$(`#dc_tbl`).DataTable({
      dom: "frtip",
      data: dcAData.data,
      lengthMenu: [96, 192, 188],
      columns: [
        { data: "plantName", title: "Plant_Name" },
        { data: "dateTime", title: "DateTime" },
        { data: "dcValue", title: "DCVal" }
      ],
    });
    
    ($(`#dcDataModal`) as any).modal('show');
    
});
}