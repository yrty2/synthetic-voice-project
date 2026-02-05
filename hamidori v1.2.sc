{~voice.()}.play
~test.()
{~voicek.()}.play
(
~voice={|f0 = 440,f01=800,f02=1200,f03=2600,f11=0,f12=0,f13=0,v1=0.2,v2=0.15,v3=0.03,mix=0,muff=false|
	if(f11==0){
		f11=f01;
		f12=f02;
		f13=f03;
	};
	{
	var vibRate=5.5;
	var vibDepth=10;
	var vib=SinOsc.ar(vibRate)*vibDepth;
	var freq=f0*pow(3,vib/1200);
	var layer2={
    var harmonics = Mix.fill(12, { |n|
				SinOsc.ar(freq * (n+1)) / ((n+1)**1.6)
    });
    var shimmer = LFNoise1.kr(4).range(0.9, 1.1);
    var breath  = WhiteNoise.ar(0.015);

			var src = (harmonics * shimmer + breath);
    src = LPF.ar(src, 3200);
			src * 0.35 ! 2;
		};
		var harmonics =layer2;
	var t = Line.kr(0, 1, 0.08);
	var f1=t.linexp(0,1,f01,f11);
	var f2=t.linexp(0,1,f02,f12);
	var f3=t.linexp(0,1,f03,f13);
	var w1 = BPF.ar(harmonics+WhiteNoise.ar(0.012), f1+ SinOsc.kr(0.3,0,30), v1);
    var w2 = BPF.ar(harmonics+WhiteNoise.ar(0.033), f2+ SinOsc.kr(0.2,0,80), v2);
    var w3 = BPF.ar(harmonics+WhiteNoise.ar(0.069), f3+ SinOsc.kr(0.1,0,50), v3);
	var env = EnvGen.kr(Env.asr(0.2, 1, 0.3), 1);
	var nasal,muffled;
	var voice=w1+w2+w3;
		if(muff){
			nasal = BPF.ar(voice, 280, 0.5) * 1.5;
			muffled = LPF.ar(voice, 1200);
			voice=nasal+muffled;
		};
		Pan2.ar((voice+mix)*env ! 2,0);
	}
};
~voicek={ |amp=1.3|
    var env = EnvGen.kr(Env.perc(0.0063, 0.03), doneAction:2);
	var noise = WhiteNoise.ar*env;
    var burst = BPF.ar(noise, 3000, 0.3);
    burst * amp ! 2;
};
~voices={ |amp=0.3, dur=0.04|
    var env = EnvGen.kr(
        Env.perc(0.18, dur, -2),
        doneAction:2
    );
	var noise = WhiteNoise.ar * env;
    var hiss = BPF.ar(noise,346, 0.25)
             + BPF.ar(noise, 2200, 0.2) * 0.6;
	hiss = HPF.ar(hiss,1100);
	amp=amp;
	(hiss * amp) ! 2;
};
~voicen={
   var  nose=PinkNoise.ar*0.4;
    var n1=BPF.ar(nose, 750, 0.3);   //鼻腔共鳴
    var n2=LPF.ar(nose, 2100);
    nose=(n1+n2)*(1-Line.kr(0, 1, 0.12));
	nose;
};
)
{~voicen.()}.play
~test.();
{~midori.(392,"","a")}.play
{~midori.(392,"","i")}.play
{~midori.(392,"","u")}.play
{~midori.(392,"","e")}.play
{~midori.(392,"","o")}.play
(
~midorivow={ |freq=316,vowel="a",mix=0,muff=false|
	switch(vowel,
		"a", {~voice.(f0: freq,mix:mix,muff:muff,f01:820,f02:1320,f03:2800)},
		"i", { ~voice.(f0: freq, f01:305, f02:2695, f03:3250,mix:mix,muff:muff) },
		"u", { ~voice.(f0: freq, f01:355, f02:1635, f03:2600,mix:mix,muff:muff) },
		"e", { ~voice.(f0:freq,mix:mix,muff:muff,
			f01:460,f02:2297,f03:2800)},
		"o",{~voice.(f0:freq,f01:790,f02:980,f03:1770,mix:mix,muff:muff)}
	)
};
~midori={ |freq=316, con="",vow="a",duration=1|
	var syn,innersyn;
	switch(con,
		"",{
			syn={~midorivow.(freq,vow)}.play;
			duration.wait;
			syn.free;
		},
		"k", {
			syn=Routine({
				{~voicek.()}.play;
				0.04.wait;
				innersyn={~midorivow.(freq, vow)}.play;
				duration.wait;
				innersyn.free;
			}).play;
			duration.wait;
			syn.free;
	},
	"s", {
			syn=Routine({
				{~voices.()}.play;
				0.04.wait;
				innersyn={~midorivow.(freq, vow)}.play;
				(duration-0.04).wait;
				innersyn.free;
			}).play;
			(duration).wait;
			syn.free;
	},
	"n", {
		syn=Routine({
				{~voicen.()}.play;
				0.02.wait;
				innersyn={~midorivow.(freq, vow)}.play;
				(duration-0.02).wait;
				innersyn.free;
			}).play;
			(duration).wait;
			syn.free;
	});
}
)
{~voicen.()}.play
//より人間の声のように聞こえる方法は？
//a,i,u,e,o
(
~test={
var shuha=366;//316;
	var con="n";
Routine({
	~midori.(shuha,con,"a");
	~midori.(shuha,con,"i");
	~midori.(shuha,con,"u");
	~midori.(shuha,con,"e");
	~midori.(shuha,con,"o");
}).play;
};
~test.();
)
{~test.()}.play;
{SinOsc.ar(316)}.play
//高木・ランズバーグ
(
{
	var freq=316;
	(Mix.fill(4, { |n|
			LFTri.ar((freq)*pow(2,n))/pow(2,n)
})*1.5-0.5)*2;
}.play
)
s.boot;

(
SynthDef(\femaleSource, { |out=0, f0=240|
    var jitter = LFNoise1.kr(6) * 0.3;
    var freq = f0 * (2 ** (jitter / 1200));

    var harmonics = Mix.fill(12, { |n|
        SinOsc.ar(freq * (n+1)) / ((n+1) ** 1.4)
    });

    var shimmer = LFNoise1.kr(4).range(0.9, 1.1);
    var breath  = WhiteNoise.ar(0.015);

    var src = (harmonics * shimmer + breath);
    src = LPF.ar(src, 6500);

    Out.ar(out, src * 0.35 ! 2);
}).add;
)