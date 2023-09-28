import { IdcAndActResp} from "./interfacesTypes/dcAndActTypes";
import { IdcDataResp } from "./interfacesTypes/dcDataTypes";

export const getDcAndActData = async (
  stateName: string
): Promise<IdcAndActResp | null> => {
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

export const getDcData = async (
  genName: string
): Promise<IdcDataResp | null> => {
  try {
    const resp = await fetch(`/api/dc/${genName}`, {
      method: "get",
    });
    const respJSON = await resp.json();
    return respJSON;
  } catch (e) {
    console.error(e);
    return null;
  }
};