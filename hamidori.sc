//ワイエルシュトラス
(
SynthDef(\voice1,{|f0 = 440,f1=800,f2=1200,f3=2600,v1=0.2,v2=0.15,v3=0.03|
	var vibRate=5.5;
	var vibDepth=12;
	var vib=SinOsc.ar(vibRate)*vibDepth;
	var freq=f0*pow(2,vib/1200);
	var layer1=Mix.fill(20, { |n|
		SinOsc.ar(((n+1)*(n+1))*freq)/((n+1)*(n+1))
	});
	var layer2=(Mix.fill(3, { |n|
		LFTri.ar(freq*pow(2,n))/pow(2,n)
})*1.5-1)*0.5;
	var layer3=Saw.ar(freq);
    var harmonics = ((layer1*0.2)+(layer2*0.4)+(layer3*0.4));
	var env=EnvGen.kr(Env.asr(0.2, 1, 0.3), 1);
	var w1 = BPF.ar(harmonics+WhiteNoise.ar(0.012), f1+ SinOsc.kr(0.3,0,30), v1);
    var w2 = BPF.ar(harmonics+WhiteNoise.ar(0.033), f2+ SinOsc.kr(0.2,0,80), v2);
    var w3 = BPF.ar(harmonics+WhiteNoise.ar(0.069), f3+ SinOsc.kr(0.1,0,50), v3);
	Out.ar(0,Pan2.ar((w1+w2+w3)*env ! 2,0))
}).add;
)
{Pulse.ar(200, 0.2)}.play
Synth(\voice1,[f0:440]);
Synth(\voice2,[f0:110]);
//フォルマント遷移あり
(
SynthDef(\voice2,{|f0 = 440,f01=800,f02=1200,f03=2600,f11=800,f12=1200,f13=2600,v1=0.2,v2=0.15,v3=0.03|
	var vibRate=5.5;
	var vibDepth=12;
	var vib=SinOsc.ar(vibRate)*vibDepth;
	var freq=f0*pow(3,vib/1200);
	var layer1=Mix.fill(20, { |n|
		SinOsc.ar(((n+1)*(n+1))*freq)/((n+1)*(n+1))
	});
	var layer2=(Mix.fill(2, { |n|
		LFTri.ar(freq*pow(2,n))/pow(2,n)
})*1.5-1)*0.5;
	var layer3=Saw.ar(freq);
    var harmonics = ((layer1*0.2)+(layer2*0.4)+(layer3*0.4));
	var t = Line.kr(0, 1, 0.08);
	var f1=t.linexp(0,1,f01,f11);
	var f2=t.linexp(0,1,f02,f12);
	var f3=t.linexp(0,1,f03,f13);
	var w1 = BPF.ar(harmonics+WhiteNoise.ar(0.012), f1+ SinOsc.kr(0.3,0,30), v1);
    var w2 = BPF.ar(harmonics+WhiteNoise.ar(0.033), f2+ SinOsc.kr(0.2,0,80), v2);
    var w3 = BPF.ar(harmonics+WhiteNoise.ar(0.069), f3+ SinOsc.kr(0.1,0,50), v3);
	 var env = EnvGen.kr(Env.asr(0.2, 1, 0.3), 1);
	Out.ar(0,Pan2.ar((w1+w2+w3)*env ! 2,0))
}).add;
)
//より人間の声のように聞こえる方法は？
//a,i,u,e,o
(
var shuha=380;//316;
Routine({
	a=Synth(\voice1,[f0:shuha]);
	1.wait;
	a.free;
	i=Synth(\voice1,[f0:shuha,f1:300,f2:2300,f3:3000]);
	1.wait;
	i.free;
	u=Synth(\voice1,[f0:shuha,f1:350,f2:900,f3:2200]);
	1.wait;
	u.free;
	e=Synth(\voice2,[f0:shuha,f01:750,f02:2200,f03:3100,f11:500,f12:2100,f13:2700,v1:0.08,v2:0.06,v3:0.02]);
	1.wait;
	e.free;
	o=Synth(\voice1,[f0:shuha,f1:450,f2:800,f3:2400,v1:0.05,v2:0.04,v3:0.02]);
	1.wait;
	o.free;
}).play;
)
{SinOsc.ar(660)}.play

//高木・ランズバーグ
(
{3/2*Mix.fill(3, { |n|
	LFTri.ar(220*pow(2,n))/pow(2,n)
})}.play
)