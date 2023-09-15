import {getDcAndActData} from "../fetchDataApi"
import { IdcAndActResp } from "../interfacesTypes/dcAndActTypes";

window.onload = async () => {
	let intervalId = null;
	intervalId = setInterval(refreshData, 1000 * 60 * 1);
	(document.getElementById('dcVsActBtn') as HTMLButtonElement).onclick = refreshData;
	refreshData()
}
const refreshData = async () => {
	
	const stateName = (document.getElementById("stateName") as HTMLSelectElement).value;
	//fetch DC And Act data and push data to datatble div created above dynamically
	let dcAndActData = await getDcAndActData(stateName);
	console.log(dcAndActData);
	$(`#dcAndAct_tbl`).DataTable().destroy();
	
    $(`#dcAndAct_tbl`).DataTable({
      dom: "",
      data: dcAndActData.data,

      
      order: [[1, "desc"]],
      columns: [
        { data: "plantName", title: "Plant_Name" },
        { data: "dateTime", title: "DateTime" },
        { data: "dcValue", title: "DCVal" },
        { data: "actualValue", title: "ActVal" },
      ],
    });
	


}