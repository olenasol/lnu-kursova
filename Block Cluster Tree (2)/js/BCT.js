var canvas,ctx,n;
var increase1=40;
var increase2=40;
var increase3=40;
var increase4=40;
var isPaused=false;

window.onload=function(){
	canvas= document.getElementById('myCanvas');
	ctx=canvas.getContext('2d');
	ctx.clearRect(0,0,canvas.width,canvas.height);
	drawSomething();
}


function drawSomething(){
	ctx.save();
	ctx.clearRect(0,0,canvas.width,canvas.height);
	n=8;
	ctx.fillStyle='black';
	var a=[];
	for(var i=0;i<n;i++){
		a[i]=i;
		ctx.fillText(a[i].toString(),50+i*(400/n),25);
		ctx.fillText(a[i].toString(),25,50+i*(400/n));
	}
	ctx.fillStyle='beige';
	ctx.strokeStyle='black';
	ctx.strokeRect(40,40,400,400);
	ctx.fillRect(40,40,400,400);
	ctx.restore();
}
function draw1(){
	ctx.moveTo(240,40);
	ctx.lineTo(240,440);
	ctx.stroke();
}
function draw2(){
	ctx.fillStyle='yellow';
	ctx.fillRect(240,40,200,100);
	ctx.fillRect(340,140,100,100);
	ctx.fillRect(40,340,200,100);
	ctx.fillRect(40,240,100,100);
	draw1();	
	ctx.moveTo(140,40);
	ctx.lineTo(140,440);
	ctx.moveTo(340,40);
	ctx.lineTo(340,440);
	horizline(140,40,440);
	horizline(340,40,440);
	ctx.stroke();
}
function draw3(){
	ctx.fillStyle='yellow';
	for (var i=0;i<=5;i++){
		ctx.fillRect(40+i*50,140+i*50,50,300-i*50);
	}
	for(var i=0;i<=5;i++){
		ctx.fillRect(140+i*50,40,50,50+i*50);
	}
	ctx.moveTo(90,40);
	ctx.lineTo(90,240);
	ctx.moveTo(390,240);
	ctx.lineTo(390,200+240);
	horizline(90,40,240);
	horizline(390,240,200+240);
	ctx.moveTo(190,40);
	ctx.lineTo(190,340);
	ctx.moveTo(290,140);
	ctx.lineTo(290,100+340);
	horizline(190,40,340);
	horizline(290,140,100+340);
	ctx.stroke();
}
function animationLoop(timeStamp){
	if(isPaused===false){
	drawSomething();
	var m=4;
	if (increase1<=440){
		increase1 +=m;
		ctx.moveTo(240,40);
		ctx.lineTo(240,increase1);	
		ctx.stroke();
		horizline(240,40,increase1);
		ctx.stroke();
	}
	else if (increase2<=440){
		increase2+=m;
		draw1();	
		ctx.moveTo(140,40);
		ctx.lineTo(140,increase2);
		ctx.moveTo(340,40);
		ctx.lineTo(340,increase2);
		horizline(140,40,increase2);
		horizline(340,40,increase2);
		ctx.stroke();
	}
	else if (increase3<=240||increase4<=340){
		draw2();
		increase3+=m;
		increase4+=m+1;
		if (increase3<=240){
			ctx.moveTo(90,40);
			ctx.lineTo(90,increase3);
			ctx.moveTo(390,240);
			ctx.lineTo(390,200+increase3);
			horizline(90,40,increase3);
			horizline(390,240,200+increase3);
			ctx.stroke();
		}
		if(increase4<=340){
			ctx.moveTo(190,40);
			ctx.lineTo(190,increase4);
			ctx.moveTo(290,140);
			ctx.lineTo(290,100+increase4);
			horizline(190,40,increase4);
			horizline(290,140,100+increase4);
			ctx.stroke();
		}
	}
	else{
		draw3();
	}
	}
	requestId = requestAnimationFrame(animationLoop);
	
}
function horizline(x,y,increase){
	ctx.moveTo(y,x);
	ctx.lineTo(increase,x);
}
function start(){
	requestId = requestAnimationFrame(animationLoop);
}
function pause(){
	isPaused=!isPaused;
}
function stop(){
	location.reload();
	if (requestId) {
        cancelAnimationFrame(requestId);
    }
}