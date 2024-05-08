import { getDcSchActData, getDcSchData } from "../fetchDataApi"
import { getLastUpdatedTime } from "../timeUtils"

window.onload = async () => {
	let intervalId = null;
	intervalId = setInterval(refreshData, 1000 * 60 * 5);
	(document.getElementById('dcVsActBtn') as HTMLButtonElement).onclick = refreshData;
	refreshData()
}
const refreshData = async () => {

	const stateName = (document.getElementById("stateName") as HTMLSelectElement).value;
	const metInfoDiv = document.getElementById("metaInfo") as HTMLDivElement;
	metInfoDiv.innerHTML = `Showing Data For ${stateName}`;
	const lastUpdatedDiv = document.getElementById("lastUpdated") as HTMLDivElement;
	lastUpdatedDiv.innerHTML = `Last update On ${getLastUpdatedTime()}`;

	// removing table rows and destroying datatable before next refresh cycle
	($(`#dcSchAct_tbl`) as any).DataTable().destroy();
	$(`#dcSchAct_tbl tbody`).empty();
	//fetch DC And Act data and push data to datatble div 
	let dcSchActData = await getDcSchActData(stateName);


	let dcSchActTbl = ($(`#dcSchAct_tbl`) as any).DataTable({
		//dom: "frtip",
		dom: "Bfrtp",
		data: dcSchActData.data,
		lengthMenu: [40, 80, 120],
		columns: [
			{ data: "dateTime", title: "DateTime", },
			{ data: "plantName", title: "Plant_Name" },
			{ data: "fuelType", title: "FuelType" },
			{ data: "installedCapacity", title: "Inst_Cap" },
			{ data: "outageCapacity", title: "Outage_Cap" },
			{ data: "runningCapacity", title: "Running_Cap" },
			{ data: "auxConsumption", title: "Aux-Cons(%)" },
			{ data: "dcValue", title: "DCVal" },
			{ data: "schValue", title: "SchVal" },
			{ data: "actualValue", title: "ActVal" },
			{ data: "diffVal", title: "Margin(DC-SCH)" },
			{ data: "partialOutage", title: "Partial Outage" },
		],
		columnDefs: [	{ width: '6%', targets: 0 },
						{ width: '6%', targets: 1 },
						{ width: '6%', targets: 2 },
						{ width: '6%', targets: 3 },
						{ width: '6%', targets: 4 },
						{ width: '6%', targets: 5 },
						{ width: '6%', targets: 6 },
						{ width: '6%', targets: 7 },
						{ width: '6%', targets: 8 },
						{ width: '6%', targets: 9 },
						{ width: '6%', targets: 10 },
						{ width: '6%', targets: 11 }],

		//must include cdn for that
		colReorder: true,
		fixedHeader: true,
		buttons: {
			//For colvis cdn was added
			buttons: ['copy', 'csv', 'excel', 'colvis']
		},
		order: [[2, 'desc']],
		// for adding sum row in footer
		footerCallback: function() {
			var api = this.api();
			//add column index in which you want to show
			let shownSumColInd = [4, 5, 7, 8, 9, 10, 11];
			$(api.column(3).footer()).html("Total");
			for (const ind of shownSumColInd) {
				// Total over this page
				let pageTotal = api
					.column(ind, { page: 'current' })
					.data()
					.reduce(function(a, b) {
						return a + b;
					}, 0);
				//setting footer  
				$(api.column(ind).footer()).html(
					pageTotal
				);
			}

		}
	});


	//dcSchTbl

	(dcSchActTbl as any).on('click', 'tbody tr', async function() {
		// removing table rows and destroying datatable before next refresh cycle
		($(`#dcSch_tbl`) as any).DataTable().destroy();
		$(`#dcSch_tbl tbody`).empty();
		const data = dcSchActTbl.row(this).data();
		
		let plantName = data['plantName']
		//fetch DC data of current clicked generator and push data to datatble div 
		let dcAData = await getDcSchData(plantName);
		($(`#dcSch_tbl`)as any).DataTable({
			dom: "frtip",
			data: dcAData.data,
			lengthMenu: [96, 192, 188],
			columns: [
				{ data: "plantName", title: "Plant_Name" },
				{ data: "dateTime", title: "DateTime" },
				{ data: "dcValue", title: "DCVal" },
				{ data: "schValue", title: "SchVal" }
			],
			
			footerCallback: function() {
			var api = this.api();
			//add column index in which you want to show
			let shownSumColInd = [2];
			$(api.column(1).footer()).html("Total");
			for (const ind of shownSumColInd) {
				
				const data = api.column(ind).data()
				var sum = data.reduce(function(a, b) {	
					return a + b;
					}, 0)
				var avg = Math.round(sum/data.length);
				$(api.column(ind).footer()).html(
					`${avg}`
						
				);
			}
		}
		});
		($(`#dcSchDataModal`) as any).modal('show');
	});
}