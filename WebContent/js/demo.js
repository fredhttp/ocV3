
 
var g_itinrow = 0;
var g_bucket = 5;
var g_pages = 20;
var g_src="";
var g_input="";
var g_id = "";
var g_hasToken = false;
var g_lat = "";
var g_lon = "";


//var base_url = "https://home-tripolay.rhcloud.com/homerest";
//var base_url = "https://demo-tripolay.rhcloud.com/homerest";
var base_url = "https://localhost:8443/rest";

var picker1 = rome(dp1,{
    weekStart: 1,
    min:rome.moment(),
    max:rome.moment().add('days', 60),
    dateValidator: rome.val.beforeEq(dp2)
});
var picker2 = rome(dp2,
    {
        weekStart: 1,
        min:rome.moment(),
        max:rome.moment().add('days', 60),
        dateValidator: rome.val.afterEq(dp1)
    }
);

var onSuccess = function(data){
     var cityname = data.city.names.en || ' ';
     g_lat = data.location.latitude || ' ';
     g_lon = data.location.longitude || ' ';
     init_src(cityname);
};

var onError = function(error){
	outputmsg("Location","Please supply departure location for flights in the search box");
	init_src("");
};

geoip2.city(onSuccess, onError);



$(document).ready(function() {
	
	init_autocomplete();
	
});


function init_autocomplete() 
{	
	jQuery("#srchinput").autocomplete({
		source: citydata,
		minLength: 3,
		select: function (event, ui) {
		 			var selectedObj = ui.item;
		 			jQuery("#srchinput").val(selectedObj.value);
					callsvc(selectedObj.value,"","");
		 			return false;
				},
		open: function () {
		 			jQuery(this).removeClass("ui-corner-all").addClass("ui-corner-top");
				},
		close: function () {
				
		 			jQuery(this).removeClass("ui-corner-top").addClass("ui-corner-all");
				}
	 	})
	 	jQuery("#srchinput").autocomplete("option", "delay", 100);
	
		jQuery("#srchinput").on('keypress', function(e){
			if (e.which == 13) {
				// e.preventDefault();
				//alert("key13");
				var input = $("#srchinput").val(); 
				callsvc(input,"","");
				return false;	
			}
		});
		
		
	$("#srchinput").val("your destination city and we will do the rest");
	$("#srchinput").on("focus", function(){
	       	$("#srchinput").val("");
		});	
}


function setup_paging() 
{
	pagearr = []; 
	for(i = 0; i < g_pages ; i++)
	{
		pagearr.push(i);
	}
	$.each(pagearr, function(index, value){
	
		var temp = "p" + ++index;
		var new_div = $("<div>").attr("id",temp).addClass("accordion").css("display","none");
		if(index == 1)
			new_div = $("<div>").attr("id",temp).addClass("accordion _current");
	
		$("#paging").before(new_div);	
	
		for(i = 0; i< g_bucket; i++)
		{
			$("<p>").addClass("0").css("margin","0").css("padding","0").css("width","80%").appendTo("#" + temp);
			$("<p>").addClass("1").css("margin","0").css("padding","0").css("width","80%").appendTo("#" + temp);
			$("<p>").addClass("2").css("margin","0").css("padding","0").css("width","80%").appendTo("#" + temp);
			$("<p>").addClass("3").appendTo("#" + temp);
		}		 						
	});
	// $('.spinner2').css({'display': 'inline-block'});
	//$('.cal-img').css({'display': 'inline-block'});
}

function callsvc(input,sdatetime,edatetime)
{
	cls();
	setup_paging();
	g_input = input;
	if(!g_id)
	{
		outputmsg("Login","<h4>Please login first.</h4>");
		return;
	}
	$('.spinner2').css({'display': 'inline-block'});
	$('.cal-img').css({'display': 'inline-block'});
	flysvc(input,sdatetime,edatetime);
	carsvc(input,sdatetime,edatetime);
	htlsvc(input,sdatetime,edatetime);
}
function init_src(cityname)
{
	g_src = cityname
}

function flysvc(input,sdatetime,edatetime)
{
	var x = {"id":g_id,"src":g_src,"lat":g_lat.toString(),"lon":g_lon.toString(),"input":input,"sdatetime":sdatetime,"edatetime":edatetime};
	var data = JSON.stringify( x ); 
	var url = base_url + "/req/fly";
	ajaxcall(url,data,"POST",render_air,render_air_error);
}
function htlsvc(input,sdatetime,edatetime)
{ 
	var x = {"id":g_id,"input":input,"sdatetime":sdatetime,"edatetime":edatetime};
	var data = JSON.stringify( x ); 
	var url = base_url + "/req/htl";
	ajaxcall(url,data,"POST",render_htl,render_htl_error);	
}
function carsvc(input,sdatetime,edatetime)
{
	var x = {"id":g_id,"input": input,"sdatetime": sdatetime,"edatetime": edatetime };
	var data = JSON.stringify( x ); 
	var url = base_url + "/req/car";
	ajaxcall(url,data,"POST",render_car,render_car_error);
}

// for POST JSON, must have header since client will not supply it automatically
function ajaxcall(url,data,type,handle_S,handle_F)
{
	 $.ajax({
	        type: type,
	        url: url,
	        data:  data,
	        dataType: "json",
	        headers: {
	            'Content-Type':'application/json'
	        	},
	        
	        //if received a response from the server
	        success: function( data, textStatus, jqXHR) {
	            
	             if(data.success)
	             {
	            	handle_S(data);
	             } 
	             else
	             {
	                 //$("#ajaxResponse").html("<div><b>data in Invalid!</b></div>");
	            	 //alert("status in : " + textStatus);
	            	 handle_F(data);
	             }
	        },
	        //If there was no response from the server
	        error: function(jqXHR, textStatus, errorThrown){
	        	 alert("No Response from Server: " + textStatus + "  " + errorThrown + " " + jqXHR.status + " " + jqXHR.statusText);
	             console.log("Something really bad happened " + textStatus);
	             $('.spinner2').css({'display': 'none'});
	            /// alert(textStatus);
	             
	        },
	        //capture the request before it was sent to server
	        beforeSend: function(jqXHR, settings){
	            
	        },
	        //this is called after the response or error functions
	        complete: function(jqXHR, textStatus){
	            
	        }
	    });   
}


function render_air(data){
	//alert(data.fltinfo.length);
	render(data.fltinfo,"#airTPL"," p.0");
	$("#paging").paginate({
	    count       : g_pages,
	    start       : 1,
	    display     : 7,
	    border                  : false,
	    text_color              : '#79B5E3',
	    background_color        : 'none',   
	    text_hover_color        : '#2573AF',
	    background_hover_color  : 'none', 
	    images      : false,
	    mouse       : 'press',
	    onChange                : function(page){
	                                $('._current','#dataContainer').removeClass('_current').hide();
	                            
	                                $('#p'+page).addClass('_current').show();
	                              }
	});
}

function render_htl(data)
{
	render(data.htlinfo,"#htlTPL"," p.1");
	$('.spinner2').css({'display': 'none'});
}
function render_car(data)
{
	render(data.carinfo,"#carTPL"," p.2");
}

function render_air_error(data) { $('.spinner2').css({'display': 'none'});}
function render_htl_error(data) { $('.spinner2').css({'display': 'none'});}
function render_car_error(data) { $('.spinner2').css({'display': 'none'});}

function render(obj,template,section)
{ 
	var currpage = 1;
	var $f = $("#dataContainer").find("#p" + currpage + section);
	var x = 0;
	$.each(obj, function(index, value) {
		if(index > currpage * g_bucket)
		{
			currpage++;
			$f = $("#dataContainer").find("#p" + currpage + section );
			x = 0;
		}
		$(template).tmpl(value,{id:index}).appendTo($f[x++]);
	});
}




function addair($parent){
	var depapt  =  $parent.find('span.depapt').text();
 	var arvapt  =  $parent.find('span.arvapt').text();
 	var fltnum  =  $parent.find('span.fltnum').text();
 	var airline =  $parent.find('span.airline').text();
 	var deptime =  $parent.find('span.deptime').text();
 	var price   =  $parent.find('span.price').text().substr(1);
 	var recnum 	=  $parent.find('span.recnum').text();
	var data = {
		"recnum": recnum,
		"itinclass": 0,
		"text": depapt + " to " + arvapt + " Flight# " + fltnum + " " + airline + " departs at: " + deptime,
		"price": price
	};
	itin_add(data);
}
function addhtl($parent){
	var chkin 	=  $parent.find('span.htlchkin').text();
 	var chkout 	=  $parent.find('span.htlchkout').text();
 	var rooms 	=  $parent.find('span.htlroom').text();
 	var nights 	=  $parent.find('span.htlnight').text();
 	var rating 	=  $parent.find('span.htlrate').text();
 	var price 	=  $parent.find('span.price').text().substr(1);
 	var recnum 	=  $parent.find('span.recnum').text();
	var data = {
		"recnum": recnum,
		"itinclass": 1,
		"text": "Check-in " + chkin + " Check-out " + chkout + " Rooms: " + rooms + " Nights: " + nights + " Star Rating: " + rating , 
		"price": price
	};
	itin_add(data);
}
function addcar($parent){
	var model 	  =  $parent.find('span.carmodel').text();
 	var airport   =  $parent.find('span.carpkapt').text();
 	var pickupday =  $parent.find('span.carpkday').text();
 	var pickuptime = $parent.find('span.carpktime').text();
 	var price 	  =  $parent.find('span.price').text().substr(1);
 	var recnum 	  =  $parent.find('span.recnum').text();                      
	var data = {
		"recnum": recnum,
		"itinclass": 2,
		"text": "Model " + model + " pick-up day " + pickupday + " time: " + pickuptime + " from " + airport,
		"price": price
	};
	itin_add(data);	
}

function itin_addrow(data)  {
	var row = $('<tr></tr>');
	var C1 = $('<td></td>').addClass('recnum').text(data.recnum);
	var C2 = $('<td></td>').addClass('recnum').text(data.itinclass);
	var C3 = $('<td></td>').text(data.text);
	var C4 = $('<td></td>').addClass('total');
    var C5 = $('<td></td>').addClass('price').text(data.price);
    var C6 = $('<td><img src="img/delete.png" class="deleteimg" /></td>');
    row.append(C1).append(C2).append(C3).append(C4).append(C5).append(C6);
    return row;
}
function itin_addtotal(total) {
	var row = $('<tr></tr>');
    var C1 = $('<td></td>').addClass('recnum');
    var C2 = $('<td></td>').addClass('recnum');
    var C3 = $('<td></td>');
	var C4 = $('<td></td>').addClass('total').text("Total");
    var C5 = $('<td></td>').addClass('totalprice').text(total);
    row.append(C1).append(C2).append(C3).append(C4).append(C5);
    $('.priceX .price__dollar').text(total);
    return row;
}
function itin_updatetotal(table) {
	var sum = 0.0;
	var $tds = $(table).find('td.price');
	$.each($tds, function()
			{
		 		sum += parseFloat($(this).text()); // no parseDouble in JS
			});
	var $td = $(table).find('tr:last').find("td").eq(4); 
	$td.text(sum.toFixed(2));	
	$('.priceX .price__dollar').text(sum.toFixed(2));
}
function itin_add(data){
	// initTable is the child of div
	if ($("#target  #itinTable").length > 0){
		var row = itin_addrow(data); 
		$('#itinTable tr:last').before(row);
		itin_updatetotal($('#itinTable'));
	}
	else{
		var table = $('<table></table>').addClass('data').attr("id","itinTable");
		row = itin_addrow(data); 
		table.append(row);
			
		row = itin_addtotal(data.price);
		table.append(row);
		$('#target').append(table); 
	}
}
function clear_data() {
	$('#target').empty();
	$("#bookbtn").hide();
	$("#msg").hide();
	$(".sub-body").hide();
}
function cls(){
	clear_data();
	$('#output').empty();
	$('#dataContainer').empty();
	new_div = $("<div>").attr("id","paging");
	$('#dataContainer').append(new_div);
	
}

function collectdata() {
	var data = [];
	var total = 0;
	var $table = $("#target  #itinTable");
	if($table.length > 0){
		$table.find('tr').each(function(index,value) { 
			if( $table.find('tr').length == index + 1) {
				total = $(this).find('td').eq(4).text();
			}
			else {
				var rec = $(this).find('td').eq(0).text();
				var itn = $(this).find('td').eq(1).text();
				var price = $(this).find('td').eq(4).text();
				data.push({ "rec": rec,
							"itn": itn,
							"price": price});
			}
		});
		jsonData = {"data":JSON.stringify(data), "total":total}
		return jsonData;
	}
	else
		$("#output").append("<p> data length: " + data.length +  "</p>");	

}


function outputmsg(title,msg)
{
    $div = $('<div id="msg" title="' + title + '">');
    $div.append(msg);

    $div.dialog({
        modal: true,
        maxHeight:500,
        show: {
        effect: "blind",
        duration: 1000
    },
    hide: {
        effect: "explode",
        duration: 1000
    },
    close: function(event, ui){
        $(this).dialog('destroy').remove();
    }
    }).prev(".ui-dialog-titlebar").css("background","#ffa41a");
}







var clientToken;
function P_init(){  
	
	var x = {"id":g_id};
	var url = base_url + "/usr/token";
	var data = JSON.stringify( x ); 
	ajaxcall(url,data,"POST",handle_token,handle_token_error);		
}

function P_send(nonce){
	var x  = collectdata();
	x.nonce = nonce;
	x.id = g_id;
	var url = base_url + "/usr/pay";
	var data = JSON.stringify( x ); 
	ajaxcall(url,data,"POST",handle_pay,handle_pay_error);		
}
function handle_token(data){
	clientToken = data.tokeninfo;
	g_hasToken = true;
	set_payment(clientToken);
}
function handle_token_error(data){
	$("#output").html("<b>Server is unavailable. Please try later.</b>");
}
function handle_pay(data){
	$("#output").html("<p>reuslt: "  + data.msg  + "</p>" );
	clear_data();
}
function handle_pay_error(data){
	$("#output").html("<div><b>Invalid data!</b>" +
			"<p>reuslt: "  + data.errmsg  + "</p>" +
		    "</div>");
}


$(".sub-body").hide();

$('body').on('click', '#bookbtn',function(e){
	if(!g_hasToken){
		P_init(); 
	}
	$(".sub-body").toggle();
});

$('body').on('click', '.accordion-toggle',function(e){
	//Expand or collapse this panel
	$(this).next().next().slideToggle('fast');
	//Hide the other panels
	$(".accordion-content").not($(this).next().next()).slideUp('fast');
});

$('body').on('click', '.chkimg',function(e){
	
	if( $('#loginimg').is(':visible'))
	{
		// for now do this for faster but there is a method to call on sever to check if login session exists.
		var msg = "<h4>To book itineraires, please login first.</h4>"; 
		var title = "Login";
		outputmsg(title,msg);
	}
	else
		{
			var $parent = $(this).parent();
		 	if($parent.hasClass('0')){
		 		addair($parent.find('h4'));
		 		$(this).hide();
		 	}
		 	else if ($parent.hasClass('1')){
		 		addhtl($parent.find('h4'));
		 		$(this).hide();
		 	}
		 	else if($parent.hasClass('2')){
		 		addcar($parent.find('h4'));
		 		$(this).hide();
		 	}
		 	$("#bookbtn").show();
		 	$("#msg").show();
		}
 	
});

// deleteimg is created on fly after DOM. Have to use body on
$('body').on('click', '.deleteimg',function(e){
	
	var price = $(this).closest("tr").find("td").eq(4).text();
	
	var rec = $(this).closest("tr").find("td").eq(0).text();
	var cl = $(this).closest("tr").find("td").eq(1).text();
	
	$("p." + cl).has("h4#" + rec).find('.chkimg').show();
	
	var $td = $('#itinTable tr:last').find("td").eq(4);
	var total = $td.text();
	
	var diff = parseFloat(total) - parseFloat(price);
	$td.text(diff.toFixed(2));
	$('.priceX .price__dollar').text(diff.toFixed(2));
	
	$(this).closest("tr").remove();
	var rowCount = $('#itinTable tr').length;
	if ( rowCount == 1){
		// only total is showing		
		clear_data();

	}
	
});

$('body').on('click', '.cal-img',function(e){
    if($('.cal-input').is(':visible'))
    {
    	$('.cal-label').css({'display': 'none'});
        $('.cal-input').css({'display': 'none'});
        $('.cal-imgadd').css({'display': 'none'});
    }
    else
    {
    	$('.cal-imgadd').css({'display': 'inline-block'});
        $('.cal-input').css({'display': 'inline-block'});
        $('.cal-label').css({'display': 'inline-block'});
    }
    
});

$('.cal-imgadd').click(function(e){
	var sdatetime = $('#dp1').val();
	var edatetime = $('#dp2').val();
	callsvc(g_input,sdatetime,edatetime);
	$('.cal-label').css({'display': 'none'});
    $('.cal-input').css({'display': 'none'});
    $('.cal-imgadd').css({'display': 'none'});
	
});



$("#disclaimer").hide();
$('#disclaimerlink').click(function(e){
	$("#disclaimer").toggle();
});




//Hosted fields
function set_payment(clientToken){
	console.log("setting payment now");

	braintree.client.create({
		authorization: clientToken
		}, function(err, clientInstance) {
				if (err) {
					console.error(err);
					return;
				}
				createHostedFields(clientInstance);
				createPayPal(clientInstance);
		});
}
function createPayPal(clientInstance) {
	
	console.log("in paypal");
	var paypalButton = document.querySelector('.paypal-button');
	console.log("in paypal after button");
	
	// Create a PayPal component.
	  braintree.paypal.create({
	    client: clientInstance
	  }, function (paypalErr, paypalInstance) {

	    // Stop if there was a problem creating PayPal.
	    // This could happen if there was a network error or if it's incorrectly
	    // configured.
	    if (paypalErr) {
	      console.error('Error creating PayPal:', paypalErr);
	      return;
	    }

	    // Enable the button.
	    paypalButton.removeAttribute('disabled');

	    // When the button is clicked, attempt to tokenize.
	    paypalButton.addEventListener('click', function (event) {

	      // Because tokenization opens a popup, this has to be called as a result of
	      // customer action, like clicking a button—you cannot call this at any time.
	      paypalInstance.tokenize({
	        flow: 'vault'
	      }, function (tokenizeErr, payload) {

	        // Stop if there was an error.
	        if (tokenizeErr) {
	          if (tokenizeErr.type !== 'CUSTOMER') {
	            console.error('Error tokenizing:', tokenizeErr);
	          }
	          return;
	        }

	        // Tokenization succeeded!
	        //paypalButton.setAttribute('disabled', true);
	        P_send(payload.nonce);

	      });

	    }, false);
	  });
}
function createHostedFields(clientInstance) {
	
	var form = document.querySelector('#cardForm');
	var submit = document.querySelector('#submit');
	
	var options = {
			client: clientInstance,
			styles: {
				'input, select': {
				'font-size': '16px',
				'font-family': 'helvetica, tahoma, calibri, sans-serif',
				'color': '#000'
				},
				':focus': {
				'color': '#000'
				},
				'.invalid': {
				'color': '#EB5757'
				}},
			fields: {
				number: {
					selector: '#card-number',
					placeholder: '4111 1111 1111 1111'},
				cvv: {
					selector: '#cvv',
					placeholder: '123'},
				expirationMonth: {
					selector: '#expiration-month',
					placeholder: 'Month',
					select: {
						options: [
			            '01 - January',
			            '02 - February',
			            '03 - March',
			            '04 - April',
			            '05 - May',
			            '06 - June',
			            '07 - July',
			            '08 - August',
			            '09 - September',
			            '10 - October',
			            '11 - November',
			            '12 - December']
					}},
				expirationYear: {
					selector: '#expiration-year',
					placeholder: 'Year',
					select: true },
				postalCode: {
					selector: '#postal-code',
					placeholder: '6000'}	
			}
		}
	
	braintree.hostedFields.create(options, function(err, hostedFieldsInstance) {
		if (err) {
			console.error(err);
			return;
		}
		// Use the Hosted Fields instance here to tokenize a card
		
		submit.removeAttribute('disabled');
		
		form.addEventListener('submit', function(event) {
			event.preventDefault();
			
			hostedFieldsInstance.tokenize(function(err, payload) {
				if (err) {
					console.error(err);
					return;
				}
  			// If this was a real integration, this is where you would
  			// send the nonce to your server.
  			P_send(payload.nonce);

			});
		});
	});
}


// Tab selection
$('.pay-select__item').on('click', function(){
  $('.pay-select__item').removeClass('is-active');
  $(this).addClass('is-active');
  
  if($(this).hasClass('pay-select--card')) {
    $('.select-body__content').removeClass('is-active');
    $('.select-body--card').addClass('is-active');
  } else {
    $('.select-body__content').removeClass('is-active');
    $('.select-body--paypal').addClass('is-active');
  }
});



	
	    

 

