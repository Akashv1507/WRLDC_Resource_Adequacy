import {ITimestampData} from './interfacesTypes/adequacyPlotData';


export const getLastUpdatedTime = (): string => {
	 const monthNames = ["January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
    ];

    const todayISTTime=getCurrentTime()
    const lastUpdatedStr = `<b>${ensureTwoDigits(todayISTTime.getHours())}:${ensureTwoDigits(todayISTTime.getMinutes())} of ${ensureTwoDigits(todayISTTime.getDate())}-${monthNames[todayISTTime.getMonth()].substring(0,3)}.</b>`
    return lastUpdatedStr
}

export const getCurrentTime = ():Date=>{
    const currentTime = new Date();
    const currentOffset = currentTime.getTimezoneOffset();
    const ISTOffset = 330;   // IST offset UTC +5:30 
    const todayISTTime = new Date(currentTime.getTime() + (ISTOffset + currentOffset)*60000);
    return todayISTTime
}

export const ensureTwoDigits = (num: number): string => {
    if (num < 10) {
        return "0" + num;
    }
    return "" + num;
}

export const toDateObj = (timestampStr: string): Date => {
    // convert 2021-09-15T04:15:00 to javascript dateobject
    let year = Number(timestampStr.substring(0, 4));
    let month = Number(timestampStr.substring(5, 7));
    let day = Number(timestampStr.substring(8, 10));
    let hour = Number(timestampStr.substring(11, 13));
    let minute = Number(timestampStr.substring(14, 16));
    let second = Number(timestampStr.substring(17, 19));
    let newTimestamp = new Date(year, month - 1, day, hour, minute, second);
    return newTimestamp;
};

// convert '2022-01-24T00:00' to '2022-01-24 00:00:00'
export const convertIsoString = (timestamp:string):string => {
let datePart = timestamp.substring(0,10)
let timepartHHMM= timestamp.substring(11,16)+':00'
let newCombineTimestamp = datePart + ' '+ timepartHHMM
return newCombineTimestamp
}

export const generateBlkTimeBwTimestamp = (startTimestamp: string, endTimestamp:string): ITimestampData[] => {
   	let dataList:ITimestampData[]=[]
    const startTimestampObj = toDateObj(startTimestamp)
    const endtTimestampObj = toDateObj(endTimestamp)
    let curTimestampObj = startTimestampObj
    while(curTimestampObj<= endtTimestampObj){
		let currTimestampStr= `${ensureTwoDigits(curTimestampObj.getFullYear())}-${ensureTwoDigits(curTimestampObj.getMonth()+1)}-${ensureTwoDigits(curTimestampObj.getDate())} ${ensureTwoDigits(curTimestampObj.getHours())}:${ensureTwoDigits(curTimestampObj.getMinutes())}:${ensureTwoDigits(curTimestampObj.getSeconds())}`
		dataList.push({timestamp:currTimestampStr, value:0})
		curTimestampObj = new Date(curTimestampObj.getTime() + 15*60000);
	}
	
	return dataList
};