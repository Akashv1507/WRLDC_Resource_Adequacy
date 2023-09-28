export interface IdcDataObj{
   
    plantName: string,
    dateTime: string,
 	dcValue:number,
    
}

export interface IdcDataResp{
	message: string,
    timestamp: string,
    status: number,
    isSuccess: boolean,
    data:IdcDataObj[]
}