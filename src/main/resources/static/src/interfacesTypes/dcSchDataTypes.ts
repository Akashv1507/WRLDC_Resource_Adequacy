export interface IDcSchDataObj{
   
    plantName: string,
    dateTime: string,
 	dcValue:number,
 	schValue:number
    
}

export interface IDcSchDataResp{
	message: string,
    timestamp: string,
    status: number,
    isSuccess: boolean,
    data:IDcSchDataObj[]
}