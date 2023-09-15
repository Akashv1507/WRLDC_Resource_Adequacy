export interface IdcAndctObj{
   
    plantName: string,
    dateTime: string,
 	dcValue:number,
    actualValue: number
}

export interface IdcAndActResp{
	message: string,
    timestamp: string,
    status: number,
    isSuccess: boolean,
    data:IdcAndctObj[]
}