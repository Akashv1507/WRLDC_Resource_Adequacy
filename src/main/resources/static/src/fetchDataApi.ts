import { IDcSchActResp} from "./interfacesTypes/dcSchActTypes";
import { IDcSchDataResp } from "./interfacesTypes/dcSchDataTypes";
import {IAdequacyPlotDataResp} from "./interfacesTypes/AdequacyPlotData"
export const getDcSchActData = async (
  stateName: string
): Promise<IDcSchActResp | null> => {
  try {
    const resp = await fetch(`/api/dcVsAct/${stateName}`, {
      method: "get",
    });
    const respJSON = await resp.json();
    return respJSON;
  } catch (e) {
    console.error(e);
    return null;
  }
};

export const getDcSchData = async (
  genName: string
): Promise<IDcSchDataResp | null> => {
  try {
    const resp = await fetch(`/api/dcSch/${genName}`, {
      method: "get",
    });
    const respJSON = await resp.json();
    return respJSON;
  } catch (e) {
    console.error(e);
    return null;
  }
};

export const getAdequacyPlotData = async (
  entityTag: string,
  startTimeStr:string,
  endTimeStr:string
): Promise<IAdequacyPlotDataResp | null> => {
  try {
    const resp = await fetch(`/api/adequacyPlotData/${entityTag}/${startTimeStr}/${endTimeStr} `, {
      method: "get",
    });
    const respJSON = await resp.json();
    return respJSON;
  } catch (e) {
    console.error(e);
    return null;
  }
};