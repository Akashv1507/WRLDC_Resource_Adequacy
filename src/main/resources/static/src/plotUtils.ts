
declare var Plotly: any;


import { toDateObj } from "./timeUtils";
import {ITimestampData} from "./interfacesTypes/adequacyPlotData"
export interface PlotTrace {
    name: string;
    // here date in string formate
    data: ITimestampData[];
    type: string;
    hoverYaxisDisplay: string;
    isSecondaryAxisTrace?:boolean;
    line?: { color?: string; width?: number };
    visible?: string | boolean;
    fill?: string;
    mode?: string;
    fillcolor?: string;
    range?:number[],
    barWidth?:number,
    offsetgroup?: number
}

export interface PlotData {
  	title: string;
    traces: PlotTrace[];
    yAxisTitle: string
    y2AxisTitle?:string
    barmode?: string
   
}

export const getPlotXYArrays = (
  measData: PlotTrace["data"]
): { timestamps: Date[]; vals: number[] } => {
  let timestamps: Date[] = [];
  let vals: number[] = [];
  for (var i = 0; i < measData.length; i = i + 1) {
    timestamps.push(toDateObj(measData[i].timestamp));
    vals.push(measData[i].value as number);
  }
  return { timestamps: timestamps, vals: vals };
};

export const setPlotTraces = (divId: string, plotData: PlotData) => {
    let traceData = [];
    // for only bar plots
    
  const layout = {
    title: {
      text: plotData.title,
      font: {
        size: 18,
        color:"#17a2b8",
      },
    },
    // plot_bgcolor:"black",
    // paper_bgcolor:"#FFF3",
    showlegend: true,
    legend: {
      orientation: "h",
      y: -0.3,
      x: 0.1,
      font: {
        family: "sans-serif",
        size: 15,
      },
    },
    hovermode: 'x',
      autosize: false,
      width: 1500,
      height: 750,

    xaxis: {
      showgrid: false,
      zeroline: true,
      showspikes: true,
      spikethickness: 1,
      showline: true,
      titlefont: { color: "#000", size: 22 },
      tickfont: { color: "#000", size: 15 },
      //tickmode: "linear",
      //dtick: 180 * 60 * 1000,
      //automargin: true,
      tickangle: 283,
    },
    yaxis: {
          title: plotData.yAxisTitle,
      showgrid: true,
      zeroline: true,
      showspikes: true,
      spikethickness: 1,
      showline: true,
      automargin: true,
      titlefont: { color: "#000"  , size: 18 },
      tickfont: { color: "#000", size: 18 },
      tickformat: "digits",
    },
    yaxis2: {
      title: plotData.y2AxisTitle,
      // range: [-20, +20],
      titlefont: {color: '#007500' , size: 18 },
      tickfont: {color: '#007500', size: 18 },
      overlaying: 'y',
      side: 'right',
      showgrid: false,
      zeroline: true,
      showspikes: true,
      spikethickness: 1,
      showline: true,
      automargin: true,
        },   
    };
    //only for secondary y axis
    if(plotData.y2AxisTitle!=null){
      layout["yaxis2"]["title"] = plotData.y2AxisTitle
    }
    //for only bar plots
    if (plotData.barmode != null) {
        layout["barmode"] = plotData.barmode
    }

  for (var traceIter = 0; traceIter < plotData.traces.length; traceIter++) {
    const trace = plotData.traces[traceIter];
    const xyData = getPlotXYArrays(trace.data);
    // creating different graph for bias error  , which is 2nd index of plotdata.traces
    let traceObj = {
      x: xyData.timestamps,
      y: xyData.vals,
      type: trace.type,
      name: trace.name,
      hovertemplate: `(%{x}, %{y:.2f} ${trace.hoverYaxisDisplay})`,
    };
    
    // defining range of bar plots
    if(trace.range!=null){
      layout["yaxis2"]["range"] = trace.range
    }
    //only for secondary y axis
    if (trace.isSecondaryAxisTrace) {
      traceObj["yaxis"] = "y2";
    }
    //only for secondary y axis
    if (trace.offsetgroup) {
      traceObj["offsetgroup"] = trace.offsetgroup;
    }
    if (trace.line != null) {
      traceObj["line"] = trace.line;
    }
    if (trace.visible != null) {
      traceObj["visible"] = trace.visible;
    }
    if (trace.fill != null) {
      traceObj["fill"] = trace.fill;
      }
    if (trace.mode != null) {
        traceObj["mode"] = trace.mode;
    }
    if (trace.barWidth != null) {
      traceObj["width"] = trace.barWidth;
  }
     
    traceData.push(traceObj);
  }
  Plotly.newPlot(divId, traceData, layout);
};