import { getAdequacyPlotData } from "../fetchDataApi";
import { PlotData, PlotTrace, setPlotTraces } from "../plotUtils";
import { convertIsoString, generateBlkTimeBwTimestamp } from '../timeUtils';
import { ITimestampData } from '../interfacesTypes/adequacyPlotData';
import { ItraceData } from "../interfacesTypes/traceData";
declare var moment: any;
window.onload = async () => {

	let currDate = new Date();
	//let yesterdayDate = new Date(currDate.setDate(currDate.getDate()-1));
	let currDateStr = currDate.toISOString().substring(0, 10);

	//setting startdate and enddate to today
	(document.getElementById("startDate") as HTMLInputElement).value = currDateStr + 'T00:00';
	(document.getElementById("endDate") as HTMLInputElement).value = currDateStr + 'T23:59';
	// settnig hydro value to 0 by default
	const hydroField = document.getElementById("hydroVal") as HTMLInputElement
	hydroField.value = '0'
	hydroField.onchange = fetchPlotData;

	const submitBtn = document.getElementById("submitBtn") as HTMLButtonElement
	submitBtn.onclick = fetchPlotData;
	
	const donwloadPlotDataBtn = document.getElementById("donwloadPlotDataBtn") as HTMLButtonElement
	donwloadPlotDataBtn.onclick = downloadPlotDataToCsv;
};


export const fetchPlotData = async () => {

	//to display error msg
	const errorDiv = document.getElementById("errorPlotDiv") as HTMLDivElement;

	//to display spinner
	const spinnerDiv = document.getElementById("plotSpinner") as HTMLDivElement;

	//get user inputs
	let startDateValue = (
		document.getElementById("startDate") as HTMLInputElement
	).value;
	let endDateValue = (document.getElementById("endDate") as HTMLInputElement)
		.value;
	const stateNameSleectElement = document.getElementById("stateName") as HTMLSelectElement
	const stateScadaId = stateNameSleectElement.value;
	const stateName = stateNameSleectElement.options[stateNameSleectElement.selectedIndex].text;
	const hydroValue = (document.getElementById("hydroVal") as HTMLInputElement).value;


	//validation checks, and displaying msg in error div
	if (startDateValue === "" || endDateValue === "" || stateScadaId == "") {
		errorDiv.classList.add("mt-4", "mb-4", "alert", "alert-danger");
		errorDiv.innerHTML = "<b> Please Enter a Valid Start Date/End Date</b>";
	}
	if (hydroValue == "") {
		errorDiv.classList.add("mt-4", "mb-4", "alert", "alert-danger");
		errorDiv.innerHTML = "<b> Please Enter a Number Value</b>";
	}
	else if (startDateValue > endDateValue) {
		errorDiv.classList.add("mt-4", "mb-4", "alert", "alert-danger");
		errorDiv.innerHTML =
			"<b> Ooops !! End Date should be greater or Equal to Start Date </b>";
	} else {
		// convert '2022-01-24T00:00' to '2022-01-24 00:00:00'
		startDateValue = convertIsoString(startDateValue)
		endDateValue = convertIsoString(endDateValue)
		//if reached this ,means no validation error ,emptying error div and making start date and end date in desired format
		errorDiv.classList.remove("mt-4", "mb-4", "alert", "alert-danger");
		errorDiv.innerHTML = "";

		//adding spinner class to spinner div
		spinnerDiv.classList.add("loader");

		try {
			const adequacyapiRespData = await getAdequacyPlotData(stateScadaId, startDateValue, endDateValue)

			if (adequacyapiRespData != null) {
				let demForecastDataList = adequacyapiRespData.data.demForecastDataList
				let solarForecastDataList = adequacyapiRespData.data.solarForecastDataList
				let windForecastDataList = adequacyapiRespData.data.windForecastDataList
				let thermalGenDataList = adequacyapiRespData.data.thermalGenDataList
				let hydroGenDataList = adequacyapiRespData.data.hydroGenDataList
				let gasGenDataList = adequacyapiRespData.data.gasGenDataList
				let wbesSdlDataList = adequacyapiRespData.data.scheduleDataList
				let fullschdRevisionNo= adequacyapiRespData.data.fullschdRevisionNo
				if (demForecastDataList.length == 0) {
					demForecastDataList = generateBlkTimeBwTimestamp(startDateValue, endDateValue)
				}
				if (solarForecastDataList.length == 0) {
					solarForecastDataList = generateBlkTimeBwTimestamp(startDateValue, endDateValue)
				}
				if (windForecastDataList.length == 0) {
					windForecastDataList = generateBlkTimeBwTimestamp(startDateValue, endDateValue)
				}
				if (thermalGenDataList.length == 0) {
					thermalGenDataList = generateBlkTimeBwTimestamp(startDateValue, endDateValue)
				}
				if (hydroGenDataList.length == 0) {
					hydroGenDataList = generateBlkTimeBwTimestamp(startDateValue, endDateValue)
				}
				if (gasGenDataList.length == 0) {
					gasGenDataList = generateBlkTimeBwTimestamp(startDateValue, endDateValue)
				}
				if (wbesSdlDataList.length == 0) {
					wbesSdlDataList = generateBlkTimeBwTimestamp(startDateValue, endDateValue)
				}

				//if hydrogen value is greater than 0 , pasting that value to all the timestamp
				if (+hydroValue > 0) {
					hydroGenDataList.forEach((singleHydorElement, ind) => {
						singleHydorElement.value = +hydroValue
					});
				}

				// calculating aux-thermal plot Data for only maharshtra and gujarat for all other state auxThermalDataList will be empty
				let auxThermalDataList: ITimestampData[] = []
				if (stateScadaId == 'WRLDCMP.SCADA1.A0046980' || stateScadaId == 'WRLDCMP.SCADA1.A0046957') {
				thermalGenDataList.forEach((ele, ind) => {
					auxThermalDataList.push({ timestamp: ele.timestamp, value: ele.value * 0.08 })
				})
				}
				// calculating Deficit plot Data	
				let deficitDataList: ITimestampData[] = []
				demForecastDataList.forEach((singleDemForecastElement, ind) => {
					let wbesSdlVal = wbesSdlDataList[ind] != null ? wbesSdlDataList[ind].value : 0
					let solarVal = solarForecastDataList[ind] != null ? solarForecastDataList[ind].value : 0
					let windVal = windForecastDataList[ind] != null ? windForecastDataList[ind].value : 0
					let thermalVal = thermalGenDataList[ind] != null ? thermalGenDataList[ind].value : 0
					let auxThermalVal = auxThermalDataList[ind] != null ? auxThermalDataList[ind].value : 0
					let hydroVal = hydroGenDataList[ind] != null ? hydroGenDataList[ind].value : 0
					let gasVal = gasGenDataList[ind] != null ? gasGenDataList[ind].value : 0
					const deficitValue = singleDemForecastElement.value - thermalVal - hydroVal - gasVal - solarVal - windVal - wbesSdlVal - auxThermalVal
					deficitDataList.push({ timestamp: singleDemForecastElement.timestamp, value: deficitValue })
				});

				let adequacyPlotData: PlotData = {
					title: `Adequacy Plots of ${stateName} B/w Time ${startDateValue} And ${endDateValue}`,
					traces: [],
					yAxisTitle: "MW",


				};
				//Demand-Forecast Trace
				let demandForecastTrace: PlotTrace = {
					name: "Demand-Forecast",
					data: demForecastDataList,
					type: "scatter",
					hoverYaxisDisplay: "Demand-Forecast",
					line: {
						width: 4,
						// color: '#34A853'
					},
					visible: "legendonly"
				};
				adequacyPlotData.traces.push(demandForecastTrace);
				
				//Schedule Trace
				let wbesSdlTrace: PlotTrace = {
					name: `Schedule_${fullschdRevisionNo}`,
					data: wbesSdlDataList,
					type: "scatter",
					hoverYaxisDisplay: "Schedule",
					line: {
						width: 4,
					},
					visible: "legendonly"
				};
				adequacyPlotData.traces.push(wbesSdlTrace);

				//Thermal Generation Trace
				let thermalGenerationTrace: PlotTrace = {
					name: "DC-Thermal",
					data: thermalGenDataList,
					type: "scatter",
					hoverYaxisDisplay: "DC-Thermal",
					line: {
						width: 4,
					},
					visible: "legendonly"
				};
				adequacyPlotData.traces.push(thermalGenerationTrace);

				if (stateScadaId == 'WRLDCMP.SCADA1.A0046980' || stateScadaId == 'WRLDCMP.SCADA1.A0046957') {

					//Aux Thermal Generation Trace for only maharashtra and gujarat
					let auxThermalGenerationTrace: PlotTrace = {
						name: "Aux-Thermal",
						data: auxThermalDataList,
						type: "scatter",
						hoverYaxisDisplay: "Aux-Thermal",
						line: {
							width: 4,
						},
						visible: "legendonly"
					};
					adequacyPlotData.traces.push(auxThermalGenerationTrace);

					//Gas Generation Trace for only maharashtra and gujarat
					let gasGenerationTrace: PlotTrace = {
						name: "DC-Gas",
						data: gasGenDataList,
						type: "scatter",
						hoverYaxisDisplay: "DC-Gas",
						line: {
							width: 4,
						},
						visible: "legendonly"
					};
					adequacyPlotData.traces.push(gasGenerationTrace);
				}

				//Hydro Generation Trace
				let hydroGenerationTrace: PlotTrace = {
					name: "DC-Hydro",
					data: hydroGenDataList,
					type: "scatter",
					hoverYaxisDisplay: "DC-Hydro",
					line: {
						width: 4,
					},
					visible: "legendonly"
				};
				adequacyPlotData.traces.push(hydroGenerationTrace);

				//Solar Forecast Trace
				let solarForecastTrace: PlotTrace = {
					name: "Solar-Forecast",
					data: solarForecastDataList,
					type: "scatter",
					hoverYaxisDisplay: "Solar-Forecast",
					line: {
						width: 4,
					},
					visible: "legendonly"
				};
				adequacyPlotData.traces.push(solarForecastTrace);
				
				if (stateScadaId != 'WRLDCMP.SCADA1.A0046945') {
				//Wind Forecast Trace
				let windForecastTrace: PlotTrace = {
					name: "Wind-Forecast",
					data: windForecastDataList,
					type: "scatter",
					hoverYaxisDisplay: "Wind-Forecast",
					line: {
						width: 4,
					},
					visible: "legendonly"
				};
				adequacyPlotData.traces.push(windForecastTrace);
				}
				
				// Deficit Trace
				let deficitTrace: PlotTrace = {
					name: "Deficit",
					data: deficitDataList,
					type: "scatter",
					hoverYaxisDisplay: "Deficit",
					line: {
						width: 6,
					},
				};
				adequacyPlotData.traces.push(deficitTrace);

				setPlotTraces(
					`adequacyPlotsWrapper`,
					adequacyPlotData
				);
			}
		}
		catch (err) {

			// removing spinner class to spinner div
			spinnerDiv.classList.remove("loader")
		}
	}
	// removing spinner class to spinner div
	spinnerDiv.classList.remove("loader")

};

export const downloadPlotDataToCsv= () =>{
	
	//to stroe data in list[rows]=>2d List
	const csvData = []
	//state name included in download file
	const stateNameSleectElement = document.getElementById("stateName") as HTMLSelectElement
	const stateName = stateNameSleectElement.options[stateNameSleectElement.selectedIndex].text;
	//getting all trace data
	const adequacyPlotsWrapperDiv=document.getElementById("adequacyPlotsWrapper") as any
	const allTraceData:ItraceData[] = adequacyPlotsWrapperDiv.data
	//getting timestamp list of 1st trace, that is x value of 1st trace
	const timestampData:Date[]= allTraceData[0].x;
	// iterating through timestamplist and genearting 2d array
	timestampData.forEach((time, ind)=>{
		let rowData=[]
		rowData.push(moment(timestampData[ind]).format("YYYY-MM-DD HH:mm:ss"))
		 for (var i = 0; i < allTraceData.length; i = i + 1) {
				    rowData.push(Math.round(allTraceData[i].y[ind]))
				  }
		csvData.push(rowData);
	})
	
	//define the heading for each row of the data  
	let csvContent = 'Timesatmp,'
	//adding column names to csvContent
	for (var i = 0; i < allTraceData.length; i = i + 1) {
				    csvContent+=allTraceData[i].name + ','
				  }
	
	csvContent = csvContent + "\r\n" 
    //merge the data with CSV  
    csvData.forEach(function(row) {  
            csvContent += row.join(',');  
            csvContent += "\r\n";  
    });  

    var hiddenElement = document.createElement('a');  
    hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csvContent);  
    hiddenElement.target = '_blank';  

    //provide the name for the CSV file to be downloaded  
    hiddenElement.download = `${stateName}_DeficitPlotData.csv`;  
    hiddenElement.click();  
}
