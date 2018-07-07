<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>


<input type="button" onclick="decode()" value="decode">


<script type="text/javascript">

function decode(){
	
	var fpdm = '3200163160'
	var json = JSON.stringify(getYzmXx(fpdm));
	alert(json)
}


function getYzmXx(fpdm) {

    var swjginfo = getSwjg(fpdm, 0);
    var url = swjginfo[1] + "/yzmQuery";
    var nowtime = showTime().toString();
    var rad = Math.random();
    var area = swjginfo[2];
    var publickey = cs.ckcode(fpdm, nowtime) 
    
    var param = {'url':url, 'fpdm': fpdm,'r': rad,'nowtime': nowtime,'area': area, 'publickey': publickey};

    return param
}

var citys = [{
    'code': '1100',
    'sfmc': '北京',
    'Ip': 'https://zjfpcyweb.bjsat.gov.cn:443',
    'address': 'https://zjfpcyweb.bjsat.gov.cn:443'
},
{
    'code': '1200',
    'sfmc': '天津',
    'Ip': 'https://fpcy.tjsat.gov.cn:443',
    'address': 'https://fpcy.tjsat.gov.cn:443'
},
{
    'code': '1300',
    'sfmc': '河北',
    'Ip': 'https://fpcy.he-n-tax.gov.cn:82',
    'address': 'https://fpcy.he-n-tax.gov.cn:82'
},
{
    'code': '1400',
    'sfmc': '山西',
    'Ip': 'https://fpcy.sx-n-tax.gov.cn:443',
    'address': 'https://fpcy.sx-n-tax.gov.cn:443'
},
{
    'code': '1500',
    'sfmc': '内蒙古',
    'Ip': 'https://fpcy.nm-n-tax.gov.cn:443',
    'address': 'https://fpcy.nm-n-tax.gov.cn:443'
},
{
    'code': '2100',
    'sfmc': '辽宁',
    'Ip': 'https://fpcy.tax.ln.cn:443',
    'address': 'https://fpcy.tax.ln.cn:443'
},
{
    'code': '2102',
    'sfmc': '大连',
    'Ip': 'https://fpcy.dlntax.gov.cn:443',
    'address': 'https://fpcy.dlntax.gov.cn:443'
},
{
    'code': '2200',
    'sfmc': '吉林',
    'Ip': 'https://fpcy.jl-n-tax.gov.cn:4432',
    'address': 'https://fpcy.jl-n-tax.gov.cn:4432'
},
{
    'code': '2300',
    'sfmc': '黑龙江',
    'Ip': 'https://fpcy.hl-n-tax.gov.cn:443',
    'address': 'https://fpcy.hl-n-tax.gov.cn:443'
},
{
    'code': '3100',
    'sfmc': '上海',
    'Ip': 'https://fpcyweb.tax.sh.gov.cn:1001',
    'address': 'https://fpcyweb.tax.sh.gov.cn:1001'
},
{
    'code': '3200',
    'sfmc': '江苏',
    'Ip': 'https://fpdk.jsgs.gov.cn:80',
    'address': 'https://fpdk.jsgs.gov.cn:80'
},
{
    'code': '3300',
    'sfmc': '浙江',
    'Ip': 'https://fpcyweb.zjtax.gov.cn:443',
    'address': 'https://fpcyweb.zjtax.gov.cn:443'
},
{
    'code': '3302',
    'sfmc': '宁波',
    'Ip': 'https://fpcy.nb-n-tax.gov.cn:443',
    'address': 'https://fpcy.nb-n-tax.gov.cn:443'
},
{
    'code': '3400',
    'sfmc': '安徽',
    'Ip': 'https://fpcy.ah-n-tax.gov.cn:443',
    'address': 'https://fpcy.ah-n-tax.gov.cn:443'
},
{
    'code': '3500',
    'sfmc': '福建',
    'Ip': 'https://fpcyweb.fj-n-tax.gov.cn:443',
    'address': 'https://fpcyweb.fj-n-tax.gov.cn:443'
},
{
    'code': '3502',
    'sfmc': '厦门',
    'Ip': 'https://fpcy.xm-n-tax.gov.cn',
    'address': 'https://fpcy.xm-n-tax.gov.cn'
},
{
    'code': '3600',
    'sfmc': '江西',
    'Ip': 'https://fpcy.jxgs.gov.cn:82',
    'address': 'https://fpcy.jxgs.gov.cn:82'
},
{
    'code': '3700',
    'sfmc': '山东',
    'Ip': 'https://fpcy.sd-n-tax.gov.cn:443',
    'address': 'https://fpcy.sd-n-tax.gov.cn:443'
},
{
    'code': '3702',
    'sfmc': '青岛',
    'Ip': 'https://fpcy.qd-n-tax.gov.cn:443',
    'address': 'https://fpcy.qd-n-tax.gov.cn:443'
},
{
    'code': '4100',
    'sfmc': '河南',
    'Ip': 'https://fpcy.ha-n-tax.gov.cn',
    'address': 'https://fpcy.ha-n-tax.gov.cn'
},
{
    'code': '4200',
    'sfmc': '湖北',
    'Ip': 'https://fpcy.hb-n-tax.gov.cn:443',
    'address': 'https://fpcy.hb-n-tax.gov.cn:443'
},
{
    'code': '4300',
    'sfmc': '湖南',
    'Ip': 'https://fpcy.hntax.gov.cn:8083',
    'address': 'https://fpcy.hntax.gov.cn:8083'
},
{
    'code': '4400',
    'sfmc': '广东',
    'Ip': 'https://fpcy.gd-n-tax.gov.cn:443',
    'address': 'https://fpcy.gd-n-tax.gov.cn:443'
},
{
    'code': '4403',
    'sfmc': '深圳',
    'Ip': 'https://fpcy.szgs.gov.cn:443',
    'address': 'https://fpcy.szgs.gov.cn:443'
},
{
    'code': '4500',
    'sfmc': '广西',
    'Ip': 'https://fpcy.gxgs.gov.cn:8200',
    'address': 'https://fpcy.gxgs.gov.cn:8200'
},
{
    'code': '4600',
    'sfmc': '海南',
    'Ip': 'https://fpcy.hitax.gov.cn:443',
    'address': 'https://fpcy.hitax.gov.cn:443'
},
{
    'code': '5000',
    'sfmc': '重庆',
    'Ip': 'https://fpcy.cqsw.gov.cn:80',
    'address': 'https://fpcy.cqsw.gov.cn:80'
},
{
    'code': '5100',
    'sfmc': '四川',
    'Ip': 'https://fpcy.sc-n-tax.gov.cn:443',
    'address': 'https://fpcy.sc-n-tax.gov.cn:443'
},
{
    'code': '5200',
    'sfmc': '贵州',
    'Ip': 'https://fpcy.gz-n-tax.gov.cn:80',
    'address': 'https://fpcy.gz-n-tax.gov.cn:80'
},
{
    'code': '5300',
    'sfmc': '云南',
    'Ip': 'https://fpcy.yngs.gov.cn:443',
    'address': 'https://fpcy.yngs.gov.cn:443'
},
{
    'code': '5400',
    'sfmc': '西藏',
    'Ip': 'https://fpcy.xztax.gov.cn:81',
    'address': 'https://fpcy.xztax.gov.cn:81'
},
{
    'code': '6100',
    'sfmc': '陕西',
    'Ip': 'https://fpcyweb.sn-n-tax.gov.cn:443',
    'address': 'https://fpcyweb.sn-n-tax.gov.cn:443'
},
{
    'code': '6200',
    'sfmc': '甘肃',
    'Ip': 'https://fpcy.gs-n-tax.gov.cn:443',
    'address': 'https://fpcy.gs-n-tax.gov.cn:443'
},
{
    'code': '6300',
    'sfmc': '青海',
    'Ip': 'https://fpcy.qh-n-tax.gov.cn:443',
    'address': 'https://fpcy.qh-n-tax.gov.cn:443'
},
{
    'code': '6400',
    'sfmc': '宁夏',
    'Ip': 'https://fpcy.nxgs.gov.cn:443',
    'address': 'https://fpcy.nxgs.gov.cn:443'
},
{
    'code': '6500',
    'sfmc': '新疆',
    'Ip': 'https://fpcy.xj-n-tax.gov.cn:443',
    'address': 'https://fpcy.xj-n-tax.gov.cn:443'
}];


function getSwjg(fpdm, ckflag) {
	var flag = "";
	var dqdm = null;
	var swjginfo = new Array();
	if (fpdm.length == 12) {
		dqdm = fpdm.substring(1, 5)
	} else {
		dqdm = fpdm.substring(0, 4)
	}
	if (dqdm != "2102" && dqdm != "3302" && dqdm != "3502" && dqdm != "3702" && dqdm != "4403") {
		dqdm = dqdm.substring(0, 2) + "00"
	}
	for (var i = 0; i < citys.length; i++) {
		if (dqdm == citys[i].code) {
			swjginfo[0] = citys[i].sfmc;
			if (flag == 'debug') {
				swjginfo[1] = "";
				swjginfo[2] = dqdm
			} else {
				swjginfo[1] = citys[i].Ip + "/WebQuery";
				swjginfo[2] = dqdm
			}
			break;	
		}
	}
	return swjginfo;
}


function showTime() {
    var myDate = new Date();
    var time = myDate.getTime();
    return time
}

cs = function (n) {
    var e, r = function (n, r) {
        return e = "402880bd5c76166f015c903ee811504e",
        n << r | n >>> 32 - r
    },
        c = function (n, r, c) {
            return e = "402880bd5c76166",
            n & c | r & ~c
        };
    return{
            ck: function (e, t, p, u, y, o) {
                var d, i = c(t, e, p),
                    f = cn.encrypt(e),
                    g = cn.encrypt(u + y),
                    a = r(e, t);
                i = 2147483648 & e,
                i += 2147483648 & t,
                i += d,
                i += d = 1073741824 & i,
                    a = i = cn.encrypt(e) + bs.encode(cn.encrypt(t)) + p;
                var b = n.gen(i, a),
                    v = n.encrypt(f) + g,
                    j = n.gen(b + n.gen(e, a) + v, g);
                return n.prijm(e, t, p, u, y, o, j)
            },
            ckcode: function (e, r) {
                var c = cn.encrypt(e + r),
                    t = cn.encrypt(e) + bs.encode(cn.encrypt(r)),
                    p = gn.gen(t, c),
                    u = cn.encrypt(c),
                    y = gn.gen(p + gn.gen(e, t) + u, t);
                 return pn.pricd(e, r, y) 

            }
        }
}();

cn = function(r) {
    var n = function(r, n) {
        return r << n | r >>> 32 - n
    }
      , t = function(r, n) {
        var t, o, e, u, f;
        return e = 2147483648 & r,
        u = 2147483648 & n,
        t = 1073741824 & r,
        o = 1073741824 & n,
        f = (1073741823 & r) + (1073741823 & n),
        t & o ? 2147483648 ^ f ^ e ^ u : t | o ? 1073741824 & f ? 3221225472 ^ f ^ e ^ u : 1073741824 ^ f ^ e ^ u : f ^ e ^ u
    }
      , o = function(r, n, t) {
        return r & n | ~r & t
    }
      , e = function(r, n, t) {
        return r & t | n & ~t
    }
      , u = function(r, n, t) {
        return r ^ n ^ t
    }
      , f = function(r, n, t) {
        return n ^ (r | ~t)
    }
      , i = function(r, e, u, f, i, a, c) {
        return r = t(r, t(t(o(e, u, f), i), c)),
        t(n(r, a), e)
    }
      , a = function(r, o, u, f, i, a, c) {
        return r = t(r, t(t(e(o, u, f), i), c)),
        t(n(r, a), o)
    }
      , c = function(r, o, e, f, i, a, c) {
        return r = t(r, t(t(u(o, e, f), i), c)),
        t(n(r, a), o)
    }
      , C = function(r, o, e, u, i, a, c) {
        return r = t(r, t(t(f(o, e, u), i), c)),
        t(n(r, a), o)
    }
      , g = function(r) {
        for (var n, t = r.length, o = t + 8, e = 16 * ((o - o % 64) / 64 + 1), u = Array(e - 1), f = 0, i = 0; i < t; )
            f = i % 4 * 8,
            u[n = (i - i % 4) / 4] = u[n] | r.charCodeAt(i) << f,
            i++;
        return n = (i - i % 4) / 4,
        f = i % 4 * 8,
        u[n] = u[n] | 128 << f,
        u[e - 2] = t << 3,
        u[e - 1] = t >>> 29,
        u
    }
      , h = function(r) {
        var n, t = "", o = "";
        for (n = 0; n <= 3; n++)
            t += (o = "0" + (r >>> 8 * n & 255).toString(16)).substr(o.length - 2, 2);
        return t
    }
      , d = function(r) {
        r = r.replace(/\x0d\x0a/g, "\n");
        for (var n = "", t = 0; t < r.length; t++) {
            var o = r.charCodeAt(t);
            o < 128 ? n += String.fromCharCode(o) : o > 127 && o < 2048 ? (n += String.fromCharCode(o >> 6 | 192),
            n += String.fromCharCode(63 & o | 128)) : (n += String.fromCharCode(o >> 12 | 224),
            n += String.fromCharCode(o >> 6 & 63 | 128),
            n += String.fromCharCode(63 & o | 128))
        }
        return n
    };
    return{
        encrypt: function(r) {
            var n, o, e, u, f, v, S, m, l, y = Array();
            for (r = d(r),
            y = g(r),
            v = 1732584193,
            S = 4023233417,
            m = 2562383102,
            l = 271733878,
            n = 0; n < y.length; n += 16)
                o = v,
                e = S,
                u = m,
                f = l,
                v = i(v, S, m, l, y[n + 0], 7, 3614090360),
                l = i(l, v, S, m, y[n + 1], 12, 3905402710),
                m = i(m, l, v, S, y[n + 2], 17, 606105819),
                S = i(S, m, l, v, y[n + 3], 22, 3250441966),
                v = i(v, S, m, l, y[n + 4], 7, 4118548399),
                l = i(l, v, S, m, y[n + 5], 12, 1200080426),
                m = i(m, l, v, S, y[n + 6], 17, 2821735955),
                S = i(S, m, l, v, y[n + 7], 22, 4249261313),
                v = i(v, S, m, l, y[n + 8], 7, 1770035416),
                l = i(l, v, S, m, y[n + 9], 12, 2336552879),
                m = i(m, l, v, S, y[n + 10], 17, 4294925233),
                S = i(S, m, l, v, y[n + 11], 22, 2304563134),
                v = i(v, S, m, l, y[n + 12], 7, 1804603682),
                l = i(l, v, S, m, y[n + 13], 12, 4254626195),
                m = i(m, l, v, S, y[n + 14], 17, 2792965006),
                S = i(S, m, l, v, y[n + 15], 22, 1236535329),
                v = a(v, S, m, l, y[n + 1], 5, 4129170786),
                l = a(l, v, S, m, y[n + 6], 9, 3225465664),
                m = a(m, l, v, S, y[n + 11], 14, 643717713),
                S = a(S, m, l, v, y[n + 0], 20, 3921069994),
                v = a(v, S, m, l, y[n + 5], 5, 3593408605),
                l = a(l, v, S, m, y[n + 10], 9, 38016083),
                m = a(m, l, v, S, y[n + 15], 14, 3634488961),
                S = a(S, m, l, v, y[n + 4], 20, 3889429448),
                v = a(v, S, m, l, y[n + 9], 5, 568446438),
                l = a(l, v, S, m, y[n + 14], 9, 3275163606),
                m = a(m, l, v, S, y[n + 3], 14, 4107603335),
                S = a(S, m, l, v, y[n + 8], 20, 1163531501),
                v = a(v, S, m, l, y[n + 13], 5, 2850285829),
                l = a(l, v, S, m, y[n + 2], 9, 4243563512),
                m = a(m, l, v, S, y[n + 7], 14, 1735328473),
                S = a(S, m, l, v, y[n + 12], 20, 2368359562),
                v = c(v, S, m, l, y[n + 5], 4, 4294588738),
                l = c(l, v, S, m, y[n + 8], 11, 2272392833),
                m = c(m, l, v, S, y[n + 11], 16, 1839030562),
                S = c(S, m, l, v, y[n + 14], 23, 4259657740),
                v = c(v, S, m, l, y[n + 1], 4, 2763975236),
                l = c(l, v, S, m, y[n + 4], 11, 1272893353),
                m = c(m, l, v, S, y[n + 7], 16, 4139469664),
                S = c(S, m, l, v, y[n + 10], 23, 3200236656),
                v = c(v, S, m, l, y[n + 13], 4, 681279174),
                l = c(l, v, S, m, y[n + 0], 11, 3936430074),
                m = c(m, l, v, S, y[n + 3], 16, 3572445317),
                S = c(S, m, l, v, y[n + 6], 23, 76029189),
                v = c(v, S, m, l, y[n + 9], 4, 3654602809),
                l = c(l, v, S, m, y[n + 12], 11, 3873151461),
                m = c(m, l, v, S, y[n + 15], 16, 530742520),
                S = c(S, m, l, v, y[n + 2], 23, 3299628645),
                v = C(v, S, m, l, y[n + 0], 6, 4096336452),
                l = C(l, v, S, m, y[n + 7], 10, 1126891415),
                m = C(m, l, v, S, y[n + 14], 15, 2878612391),
                S = C(S, m, l, v, y[n + 5], 21, 4237533241),
                v = C(v, S, m, l, y[n + 12], 6, 1700485571),
                l = C(l, v, S, m, y[n + 3], 10, 2399980690),
                m = C(m, l, v, S, y[n + 10], 15, 4293915773),
                S = C(S, m, l, v, y[n + 1], 21, 2240044497),
                v = C(v, S, m, l, y[n + 8], 6, 1873313359),
                l = C(l, v, S, m, y[n + 15], 10, 4264355552),
                m = C(m, l, v, S, y[n + 6], 15, 2734768916),
                S = C(S, m, l, v, y[n + 13], 21, 1309151649),
                v = C(v, S, m, l, y[n + 4], 6, 4149444226),
                l = C(l, v, S, m, y[n + 11], 10, 3174756917),
                m = C(m, l, v, S, y[n + 2], 15, 718787259),
                S = C(S, m, l, v, y[n + 9], 21, 3951481745),
                v = t(v, o),
                S = t(S, e),
                m = t(m, u),
                l = t(l, f);
            return (h(v) + h(S) + h(m) + h(l)).toLowerCase()
        }
    }
}();


bs = function(r) {
    function t(r, t) {
        var e = h.indexOf(r.charAt(t));
        if (-1 === e)
            throw "Cannot decode encrypt";
        return e
    }
    function e(r, t) {
        var e = r.charCodeAt(t);
        if (e > 255)
            throw "INVALID_CHARACTER_ERR: DOM Exception 5";
        return e
    }
    var n = "="
      , h = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    return {
        decode: function(r) {
            var e, h, a = 0, c = r.length, o = [];
            if (r = String(r),
            0 === c)
                return r;
            if (c % 4 != 0)
                throw "Cannot decode base";
            for (r.charAt(c - 1) === n && (a = 1,
            r.charAt(c - 2) === n && (a = 2),
            c -= 4),
            e = 0; e < c; e += 4)
                h = t(r, e) << 18 | t(r, e + 1) << 12 | t(r, e + 2) << 6 | t(r, e + 3),
                o.push(String.fromCharCode(h >> 16, h >> 8 & 255, 255 & h));
            switch (a) {
            case 1:
                h = t(r, e) << 18 | t(r, e + 1) << 12 | t(r, e + 2) << 6,
                o.push(String.fromCharCode(h >> 16, h >> 8 & 255));
                break;
            case 2:
                h = t(r, e) << 18 | t(r, e + 1) << 12,
                o.push(String.fromCharCode(h >> 16))
            }
            return o.join("")
        },
        encode: function(r) {
            if (1 !== arguments.length)
                throw "SyntaxError: exactly one argument required";
            var t, a, c = [], o = (r = String(r)).length - r.length % 3;
            if (0 === r.length)
                return r;
            for (t = 0; t < o; t += 3)
                a = e(r, t) << 16 | e(r, t + 1) << 8 | e(r, t + 2),
                c.push(h.charAt(a >> 18)),
                c.push(h.charAt(a >> 12 & 63)),
                c.push(h.charAt(a >> 6 & 63)),
                c.push(h.charAt(63 & a));
            switch (r.length - o) {
            case 1:
                a = e(r, t) << 16,
                c.push(h.charAt(a >> 18) + h.charAt(a >> 12 & 63) + n + n);
                break;
            case 2:
                a = e(r, t) << 16 | e(r, t + 1) << 8,
                c.push(h.charAt(a >> 18) + h.charAt(a >> 12 & 63) + h.charAt(a >> 6 & 63) + n);
            }
            return c.join("")
        },
        VERSION: "1.0"
    }
}();


gn = function(e) {
    var n = function() {
        var e = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth
          , n = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
        if (e * n <= 12e4)
            return !0;
        var r = window.screenX
          , c = window.screenY;
        return r + e <= 0 || c + n <= 0 || r >= window.screen.width || c >= window.screen.height
    }
      , r = function(r) {
        wzwschallenge = cn.encrypt("rhrewrchb"),
        wzwschallengex = bs.encode("rhrewrchb"),
        encoderchars = cn.encrypt(wzwschallenge) + cn.encrypt(wzwschallengex);
        var t, h, o, d, i, a;
        for (o = r.length,
        h = 0,
        t = ""; h < o; ) {
            if (d = 255 & r.charCodeAt(h++),
            h == o) {
                t += encoderchars.charAt(d >> 2),
                t += encoderchars.charAt((3 & d) << 4),
                t += "==";
                break
            }
            if (i = r.charCodeAt(h++),
            h == o) {
                t += encoderchars.charAt(d >> 2),
                t += encoderchars.charAt((3 & d) << 4 | (240 & i) >> 4),
                t += encoderchars.charAt((15 & i) << 2),
                t += "=";
                break
            }
            a = r.charCodeAt(h++),
            t += encoderchars.charAt(d >> 2),
            t += encoderchars.charAt((3 & d) << 4 | (240 & i) >> 4),
            t += encoderchars.charAt((15 & i) << 2 | (192 & a) >> 6),
            t += encoderchars.charAt(63 & a)
        }
        var w = 0;
        return n() || (w = c(wzwschallenge, wzwschallengex)),
        t + w
    }
      , c = function(e, n) {
        var r = e + n
          , c = 0
          , t = 0;
        for (t = 0; t < r.length; t++)
            c += r.charCodeAt(t);
        return c *= 245,
        c += 963863
    };
   return{
        gen: function(n, r) {
            var c, t, h, o = (o = n.trim()).trim().length, d = cn.encrypt(n), i = cn.encrypt(n) + r;
            for ((t = new Array)[0] = "ff8080815ed2f53b015f27c2b7b9783e",
            t[1] = "402880bd5c76166f015c9041698e5099",
            t[2] = "402880bd5c76166f015c903ee811504e",
            c = 0; c < t.length; ++c)
                h = t[c];
            return (window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth) * (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight) <= 12e4 ? cn.encrypt(d + i + h).toUpperCase() : cn.encrypt(d + i + h + o).toUpperCase()
        },
        moveTo: function(n) {
            return cn.encrypt(r(n))
        }
    }
}();


pn = function(e) {
    var o = function(e) {
        return 12 == e.length ? dqdm = e.substring(1, 5) : dqdm = e.substring(0, 4),
        "2102" != dqdm && "3302" != dqdm && "3502" != dqdm && "3702" != dqdm && "4403" != dqdm && (dqdm = dqdm.substring(0, 2) + "00"),
        dqdm
    }
      , r = function(e) {
        return e.length >= 12 ? e.substring(0, 10) : e.substring(0, 2)
    }
      , p = function(e) {
        return e.length + 5 * e.length
    };
    return{
        prijm: function(r, p, a, t, s, c, m) {
            var n = o(r);
            switch (n) {
/*             case "1100":
                m = cn.encrypt(r + gn.moveTo(m)).toUpperCase(),
                "0" == swjgmcft && (m = cn.encrypt(r + gn.moveTo(m) + r).toUpperCase());
                break;
            case "1200":
                m = cn.encrypt(r + gn.moveTo(m) + p).toUpperCase();
                break;
            case "1300":
                m = cn.encrypt(a + gn.moveTo(m) + gn.moveTo(r)).toUpperCase();
                break;
            case "1400":
                m = cn.encrypt(gn.moveTo(m) + s).toUpperCase(),
                "0" == swjgmcft && (m = e.encrypt(e.moveTo(m) + s + m).toUpperCase());
                break;
            case "1500":
                m = e.encrypt(e.moveTo(m) + e.moveTo(r) + p).toUpperCase();
                break;
            case "2100":
                m = e.encrypt(r + p + m).toUpperCase();
                break;
            case "2102":
                m = e.encrypt(r + e.moveTo(p) + m).toUpperCase();
                break;
            case "2200":
                m = e.encrypt(r + m + e.moveTo(m)).toUpperCase();
                break;
            case "2300":
                m = e.encrypt(s + m).toUpperCase();
                break;
            case "3100":
                m = e.encrypt(e.moveTo(m)).toUpperCase(),
                "0" == swjgmcft && (m = e.encrypt(e.moveTo(m) + p).toUpperCase());
                break; */
            case "3200":
                m = cn.encrypt(r + m).toUpperCase();
                break;
/*             case "3300":
                m = e.encrypt(p + m).toUpperCase();
                break;
            case "3302":
                m = e.encrypt(e.moveTo(p) + m).toUpperCase();
                break;
            case "3400":
                m = e.encrypt(r + e.moveTo(a) + m).toUpperCase();
                break;
            case "3500":
                m = e.encrypt(n + e.moveTo(a) + m).toUpperCase();
                break;
            case "3502":
                m = e.encrypt(n + e.moveTo(m) + r).toUpperCase();
                break;
            case "3600":
                m = e.encrypt(e.encrypt(m) + p + a).toUpperCase();
                break;
            case "3700":
                m = e.encrypt(m + e.moveTo(n) + r).toUpperCase(),
                "0" == swjgmcft && (m = e.encrypt(e.moveTo(m) + n).toUpperCase());
                break;
            case "3702":
                m = e.encrypt(e.encrypt(p) + e.moveTo(m) + r).toUpperCase();
                break;
            case "4100":
                m = e.encrypt(e.moveTo(m) + e.moveTo(r) + m).toUpperCase();
                break;
            case "4200":
                m = e.encrypt(a + e.moveTo(a) + m).toUpperCase();
                break;
            case "4300":
                m = e.encrypt(e.moveTo(a) + m + p).toUpperCase();
                break;
            case "4400":
                m = e.encrypt(e.moveTo(r) + e.moveTo(s) + m).toUpperCase();
                break;
            case "4403":
                m = e.encrypt(n + e.moveTo(m) + m).toUpperCase();
                break;
            case "4500":
                m = e.encrypt(n + e.moveTo(r) + m + a).toUpperCase();
                break;
            case "4600":
                m = e.encrypt(p + e.moveTo(r) + m + a).toUpperCase();
                break;
            case "5000":
                m = e.encrypt(r + e.moveTo(r) + r + m).toUpperCase();
                break;
            case "5100":
                m = e.encrypt(a + e.moveTo(r) + p + m).toUpperCase();
                break;
            case "5200":
                m = e.encrypt(t + e.moveTo(r) + m).toUpperCase();
                break;
            case "5300":
                m = e.encrypt(a + e.moveTo(r) + m).toUpperCase(),
                "0" == swjgmcft && (m = e.encrypt(e.moveTo(m) + r).toUpperCase());
                break;
            case "5400":
                m = e.encrypt(n + e.moveTo(r) + m).toUpperCase();
                break;
            case "6100":
                m = e.encrypt(n + m + e.moveTo(p) + e.moveTo(r)).toUpperCase();
                break;
            case "6200":
                m = e.encrypt(a + e.moveTo(p) + m).toUpperCase();
                break;
            case "6300":
                m = e.encrypt(r + e.moveTo(n) + m).toUpperCase();
                break;
            case "6400":
                m = e.encrypt(p + e.moveTo(s) + m).toUpperCase();
                break;
            case "6500":
                m = e.encrypt(t + e.moveTo(m) + m).toUpperCase() */
            }
            return m
        },
        pricd: function(a, t, s) {
            var c = o(a);
            switch (c) {
/*             case "1100":
                s = e.encrypt(a + e.moveTo(t) + s).toUpperCase(),
                "0" == swjgmcft && (s = e.encrypt(e.moveTo(s) + a).toUpperCase());
                break;
            case "1200":
                s = e.encrypt(a + e.moveTo(a + t) + s).toUpperCase();
                break;
            case "1300":
                s = e.encrypt(a + e.moveTo(a.substring(2, 3) + t) + s).toUpperCase(),
                "0" == swjgmcft && (s = e.encrypt(e.moveTo(s) + a + t).toUpperCase());
                break;
            case "1400":
                s = e.encrypt(a + e.moveTo(t) + s).toUpperCase();
                break;
            case "1500":
                s = e.encrypt(t + e.moveTo(t + s) + s.length).toUpperCase();
                break;
            case "2100":
                s = e.encrypt(s + e.moveTo(c) + s.toLowerCase()).toUpperCase();
                break;
            case "2102":
                s = e.encrypt(e.moveTo(a + t) + e.moveTo(a) + s).toUpperCase();
                break;
            case "2200":
                s = e.encrypt(a.length + t.length + e.moveTo(s) + s).toUpperCase();
                break;
            case "2300":
                s = e.encrypt(e.encrypt(a) + e.moveTo(c) + e.moveTo(s.substring(10, 15))).toUpperCase(),
                "0" == swjgmcft && (s = e.encrypt(e.moveTo(s) + t + c).toUpperCase());
                break;
            case "3100":
                s = e.encrypt(t + e.moveTo(a) + s + a).toUpperCase();
                break; */
            case "3200":
                s = cn.encrypt(gn.moveTo(c) + gn.moveTo(c) + s).toUpperCase();
                break;
/*             case "3300":
                s = e.encrypt(r(a) + e.moveTo(c) + s).toUpperCase();
                break;
            case "3302":
                s = e.encrypt(e.moveTo(a) + e.moveTo(r(c)) + s).toUpperCase();
                break;
            case "3400":
                s = e.encrypt(a + r(e.moveTo(c)) + r(s)).toUpperCase(),
                "0" == swjgmcft && (s = e.encrypt(e.moveTo(s) + t + r(s)).toUpperCase());
                break;
            case "3500":
                s = e.encrypt(r(a) + e.moveTo(c) + s).toUpperCase();
                break;
            case "3502":
                s = e.encrypt(r(a + e.moveTo(c)) + e.moveTo(s)).toUpperCase();
                break;
            case "3600":
                s = e.encrypt(p(a) + e.moveTo(s) + p(s)).toUpperCase();
                break;
            case "3700":
                s = e.encrypt(p(a) + e.moveTo(c) + r(s + a)).toUpperCase();
                break;
            case "3702":
                s = e.encrypt(e.moveTo(a + e.moveTo(c)) + s).toUpperCase();
                break;
            case "4100":
                s = e.encrypt(a + e.moveTo(e.moveTo(c) + s) + p(t)).toUpperCase();
                break;
            case "4200":
                s = e.encrypt(t + e.moveTo(e.moveTo(r(s)) + s) + r(t)).toUpperCase();
                break;
            case "4300":
                s = e.encrypt(e.moveTo(a) + e.moveTo(c) + s + p(s)).toUpperCase();
                break;
            case "4400":
                s = e.encrypt(e.encrypt(a + e.moveTo(c)) + e.moveTo(s)).toUpperCase();
                break;
            case "4403":
                s = e.encrypt(e.moveTo(a + e.moveTo(s)) + e.encrypt(s)).toUpperCase();
                break;
            case "4500":
                s = e.encrypt(e.moveTo(e.moveTo(a) + e.moveTo(c) + s)).toUpperCase(),
                "0" == swjgmcft && (s = e.encrypt(e.moveTo(s) + t + e.moveTo(c)).toUpperCase());
                break;
            case "4600":
                s = e.encrypt(e.moveTo(r(a) + p(t)) + e.moveTo(a) + s).toUpperCase();
                break;
            case "5000":
                s = e.encrypt(e.moveTo(a) + p(a) + e.moveTo(t) + s).toUpperCase();
                break;
            case "5100":
                s = e.encrypt(p(a) + e.moveTo(s + a) + r(s)).toUpperCase();
                break;
            case "5200":
                s = e.encrypt(e.moveTo(s) + e.moveTo(s + t) + s).toUpperCase();
                break;
            case "5300":
                s = e.encrypt(e.moveTo(a + e.moveTo(t)) + s).toUpperCase();
                break;
            case "5400":
                s = e.encrypt(p(e.moveTo(a)) + e.moveTo(c) + s).toUpperCase();
                break;
            case "6100":
                s = e.encrypt(r(a + e.moveTo(c)) + e.moveTo(s) + r(a + t)).toUpperCase();
                break;
            case "6200":
                s = e.encrypt(e.moveTo(a + e.moveTo(a + t + s)) + s).toUpperCase();
                break;
            case "6300":
                s = e.encrypt(e.moveTo(e.moveTo(r(a) + c)) + a + p(a + t)).toUpperCase(),
                "0" == swjgmcft && (s = e.encrypt(e.moveTo(s) + t + a + p(a + t)).toUpperCase());
                break;
            case "6400":
                s = e.encrypt(e.moveTo(a) + s + e.moveTo(c + p(a)) + s).toUpperCase();
                break;
            case "6500":
                s = e.encrypt(e.moveTo(t) + e.moveTo(e.moveTo(a + s)) + s).toUpperCase() */
            }
            return s
        }
    }
}();




</script>

</head>
<body>
</body>
</html>