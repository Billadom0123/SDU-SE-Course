function formatLocation(latitude, longitude) {
	if (typeof longitude === 'string' && typeof latitude === 'string') {
		longitude = parseFloat(longitude)
		latitude = parseFloat(latitude)
	}
	return {
		longitude: longitude,
		latitude: latitude
	}
}

//进行经纬度转换为距离的计算
function Rad(d) {
		return d * Math.PI / 180.0; //经纬度转换成三角函数中度分表形式。
	}

/*
 计算距离，参数分别为第一点的纬度，经度；第二点的纬度，经度
 默认单位km
*/
function getMapDistance(lat1, lng1, lat2, lng2) {
	
	
	var radLat1 = Rad(lat1);
	var radLat2 = Rad(lat2);
	var a = radLat1 - radLat2;
	var b = Rad(lng1) - Rad(lng2);
	var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
		Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
	s = s * 6378.137; // EARTH_RADIUS;
	s = Math.round(s * 10000) / 10000; //输出为公里
	//s=s.toFixed(2);
	return s;
}
//截取字符串小数点后   保持位数
function truncateDecimal(str, maxDecimalPlaces) {
	  const match = str.match(/^\d+\.\d+/);
	  if (match) {
		const truncated = parseFloat(match[0]).toFixed(maxDecimalPlaces);
		return truncated;
	  }
	  return str;
}

export{
	formatLocation,getMapDistance,Rad,truncateDecimal
}

