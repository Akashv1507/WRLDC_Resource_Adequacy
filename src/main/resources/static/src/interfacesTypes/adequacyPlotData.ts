export interface IAdequacyPlotDataObj{
   
    demForecastDataList: ITimestampData[],
    solarForecastDataList: ITimestampData[],
    windForecastDataList: ITimestampData[],
 	thermalGenDataList:ITimestampData[],
 	hydroGenDataList:ITimestampData[],
    gasGenDataList: ITimestampData[],
    scheduleDataList:ITimestampData[],
    fullschdRevisionNo:number
}

export interface IAdequacyPlotDataResp{
	message: string,
    timestamp: string,
    status: number,
    isSuccess: boolean,
    data:IAdequacyPlotDataObj
}


export interface ITimestampData{
	value:number,
	timestamp:string
}