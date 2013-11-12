function map(){
	var cname = this.name;
	var csize = this.size;
	var ctshirtcolor = this.tshirtcolors;
	emit(this.countries, {
		name : cname,
		nameArray : [],
		size :  csize,
		tshirtcolor : ctshirtcolor,
		colorsArray : [],
		colorsFreqArray: [],
		numAdded :  1,
		reduced : 0
		});
	}
	
function reduce(key, values){
	var retObj = {
		name :  "",
		nameArray : [],
		size :  0,
		tshirtcolor : "",
		colorsArray : [],
		colorsFreqArray: [],
		numAdded :  0,
		reduced : 1
		};
	for(var i in values){
		if(values[i].reduced == 1){
				retObj.nameArray.concat(values[i].nameArray);
			for(var j in values[i].colorsArray){
					var value=values[i].colorsArray[j];
					if(retObj.colorsArray.indexOf(value) != -1){
						retObj.colorsFreqArray[j]+=values[i].colorsFreqArray[j];
					}else{
						retObj.colorsArray.push(value);
						retObj.colorsFreqArray.push(values[i].colorsFreqArray[j]);
					}
					}
			}else{
			if(retObj.nameArray.indexOf(values[i].name) == -1){
				retObj.nameArray.push(values[i].name);
				}	
			if(retObj.colorsArray.indexOf(values[i].tshirtcolor) != -1){
					retObj.colorsFreqArray[retObj.colorsArray.indexOf(values[i].tshirtcolor)]++;
				}else{
					retObj.colorsArray.push(values[i].tshirtcolor);
					retObj.colorsFreqArray.push(1);
				}
			}
		retObj.size+=values[i].size;
		retObj.numAdded += values[i].numAdded;
		}
	return retObj;
	}
	
function finalize(key, value){
			var index =0;
			var freq =0;
			var tshirtcolor = "";
			for(var i in value.colorsArray){
				if(value.colorsFreqArray[i] > freq){
					index = i;
					freq = value.colorsFreqArray[i];
					tshirtcolor = value.colorsArray[i];
					}	
				}
	var retObj={
			names : value.nameArray,
			tshirtcolor : tshirtcolor,
			tshirtfreq : freq,
			average_size: value.size/value.numAdded
			};	
			return retObj;
	}
