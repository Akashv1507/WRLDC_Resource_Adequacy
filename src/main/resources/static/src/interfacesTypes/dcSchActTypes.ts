export interface IDcSchActObj{
   
    plantName: string,
    dateTime: string,
    installedCapacity: number,
    outageCapacity:number,
    runningCapacity: number,
    auxConsumption: number,
 	dcValue:number,
 	schValue:number,
    actualValue: number,
    diffVal:number,
    partialOutage:number
    fuelType:string
    
}

export interface IDcSchActResp{
	message: string,
    timestamp: string,
    status: number,
    isSuccess: boolean,
    data:IDcSchActObj[]
}