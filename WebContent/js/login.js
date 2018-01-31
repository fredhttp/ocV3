
g_loginform_click = 0;
g_regform_click = 0;



emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;


$('.close').on('click', function() {
  $('.container').stop().removeClass('active');
  
});

$('.footer').on('click', function() {
  $('.container').stop().addClass('active');
});


tips = $( ".info" );
function updateTips( t) {
	$( ".info" ).css("visibility","visible");
      tips.text( t ).addClass( "ui-state-highlight" );
      setTimeout(function() {
        tips.removeClass( "ui-state-highlight", 1500 );
      }, 500 );
      setTimeout(function(){$( ".info" ).css("visibility","hidden"); },2000);    
}
function checkLength( o, n, min, max ) {
      if ( o.val().length > max || o.val().length < min ) {
        o.addClass( "ui-state-error" );
        updateTips( "Length of " + n + " must be between " + min + " and " + max + "." );
        return false;
      } else {
        return true;
      }
}
function checkRegexp( o, regexp, n ) {
      if ( !( regexp.test( o.val() ) ) ) {
        o.addClass( "ui-state-error" );
        updateTips( n );
        return false;
      } else {
        return true;
      }
}

$('#dialog').dialog({ 
	autoOpen: false,
	height: 'auto',
	width: 'auto',
	show: { effect: "blind", duration: 1000},
	hide: { effect: "explode",duration: 1000}
	        
});



$("#loginimg").click(function(){
	$("#dialog").dialog("open").css({overflow:"hidden"});;
});


$("#logoutimg").click(function(){
	$('#loginimg').toggle();
	$('#logoutimg').toggle();
	cls();
	g_id = "";
});



$("#loginform").submit(function(event) {
	g_loginform_click++;
	
	if(g_loginform_click > 3){
		msg = "<h4>You have exceeded the number of logins.</h4>";  
		title = "Login";
		outputmsg(title,msg);
		$('#dialog').dialog( "close" );
		return false;
	} 
	
		event.preventDefault();
		var valid=true;
   
		email = $( "#email" );
		passwd = $( "#passwd" );
		valid = valid && checkLength( email, "email", 6, 80 );
		valid = valid && checkRegexp( email, emailRegex, "eg. you@example.com" );
    
		if ( valid ) {
			$('#dialog').dialog( "close" );
			//$('.spinner').show();
			var url = base_url + "/usr/login";
			var x = { userid: email.val(), passwd:passwd.val() };
			var data = JSON.stringify( x ); 
			ajaxcall(url,data,"POST",render_login,render_login_error);
		}
		else {
			$('#email').val("");
			return valid;
		}
	
});

function render_login(data){
	$('#loginimg').toggle();
	$('#logoutimg').toggle();
	if(data.success)
		g_id = data.sess;
	else
		g_id = "";
	
	// set the message with user name.
}
function render_login_error(data){
	msg = "<h4>Credentials not verified or exceeded number of attempts.</h4>" +
	"<h5>Please email support@tripolay.com for assistance.</h5>";  
	title = "Login";
	outputmsg(title,msg);
	
}
function render_logout(data){
	cls();
	setup_paging();
}
function render_logout_error(data){
	render_logout(data);
}



$("#regform").submit(function(event) {
	
	g_regform_click++;
	if(g_regform_click > 3)
	{
		msg = "<h4>You have exceeded the number of attempts to register sucessfully.</h4>";  
		title = "Register";
		outputmsg(title,msg);
		$('#dialog').dialog( "close" );
		return false;
	}
	event.preventDefault();
	//$('#regform :button').attr("disabled", true);
	//$('.spinner').show();
	
    var valid=true;
    email = $( "#email2" );
    
    valid = valid && checkLength( email, "email", 6, 80 );
    valid = valid && checkRegexp( email, emailRegex, "eg. you@example.com" );
    if ( valid ) {
        $('#dialog').dialog( "close" );
        $('.spinner').show();
        //$(".spinner").toggle().css("display","block");
        
        var data = JSON.stringify({email: email.val()});
        var url = base_url + "/usr/reg";
        ajaxcall(url,data,"POST",render_reg,render_reg_error);
    	//$('#regform :button').attr("disabled", false); 
    }
    else
    {
        $('#email2').val("");
        return false;
    }
	  
});

function render_reg(data)
{
	msg = "<p>Please check your email to complete registeration</p>";
	title = "Email sent";
	outputmsg(title,msg);
}
function render_reg_error(data)
{
	msg = "<p>No email was sent. Could not verify email address</p>" + data.msg; 
	title = "Email";
	outputmsg(title,msg);
}

function outputmsg(title,msg)
{
    $div = $('<div id="msg" title="' + title + '">').css("display","inline");
   
    $('.spinner').hide();
    
    $div.append(msg);
    $div.dialog({
        modal: true,
        height: 450,
        width: 500,
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



