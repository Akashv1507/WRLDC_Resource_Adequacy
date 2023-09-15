import { IdcAndActResp} from "./interfacesTypes/dcAndActTypes";

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