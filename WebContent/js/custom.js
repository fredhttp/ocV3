jQuery(function($) {
    /*=============================================================
        Authour URI: www.binarytheme.com
        License: Commons Attribution 3.0
    
        http://creativecommons.org/licenses/by/3.0/
    
        100% To use For Personal And Commercial Use.
        IN EXCHANGE JUST GIVE US CREDITS AND TELL YOUR FRIENDS ABOUT US
       
        ========================================================  */
    /*==========================================
    CUSTOM SCRIPTS
    =====================================================*/

	$('#b1').css('cursor', 'pointer');
	$('#b2').css('cursor', 'pointer');
	$('#b3').css('cursor', 'pointer');
	$('#b4').css('cursor', 'pointer');
	$('#b5').css('cursor', 'pointer');
	$('#b6').css('cursor', 'pointer');
	   
	$('#b1').click(function () {
	       $('html, body').animate({
	            scrollTop: $("#header-section").offset().top
	        }, 800);   
	    });
	    
	$('#b2').click(function () {
	        $('html, body').animate({
	            scrollTop: $("#services-section").offset().top
	        }, 800);   
	 });
	    
	$('#b3').click(function () {
	        $('html, body').animate({
	            scrollTop: $("#mission-section").offset().top
	        }, 800);   
	    });
	    
	$('#b4').click(function () {
	        $('html, body').animate({
	            scrollTop: $("#about-section").offset().top
	        }, 800);   
	    });
	
	$('#b5').click(function () {
	        $('html, body').animate({
	            scrollTop: $("#contact-section").offset().top
	        }, 800);   
	    });
	
	$('#b6').click(function () {
        $('html, body').animate({
            scrollTop: $("#services-section").offset().top
        }, 800);   
    });

    /*==========================================
   SCROLL REVEL SCRIPTS
   =====================================================*/

    
       window.scrollReveal = new scrollReveal(); 
    

    /*==========================================
    WRITE  YOUR  SCRIPTS BELOW
    =====================================================*/

});