/*
 * jQuery UI Notification Message
 *
 * Depends:
 *	    ui.core.js
 */
 
(function($) {
$.widget("ui.notificationmsg", {
    
    init: function() {
        $.ui.notificationmsg._bottompost=this.element.css("bottom");
        $.ui.notificationmsg._height=this.element.css("height");  
    },
    
    show: function(){
        var o = this.options;
        if(this.element.is(":hidden")){
            this.element.queue(function(){$.ui.notificationmsg.animations[o.animation](this, o);}).dequeue();
        }            
    },
    
    hide: function(){
        this.element.stop(true);
        var o = this.options;
        if(this.element.is(":visible")){
            this.element.queue(function(){$.ui.notificationmsg.animations[o.animation](this, o);}).dequeue();
        }
    }
});    
$.ui.notificationmsg._bottompost = "0px";
$.ui.notificationmsg._css;
$.extend($.ui.notificationmsg, {
    defaults: {
        // provide a speed for the animation
        speed: 1000,
        // provide a period for the popup to keep showing
        period: 2000, 
        // default the animation algorithm to the basic slide
        animation:'slide'
    },
    animations: {
        slide: function(e, options) {
            if($(e).is(":hidden")){
                
                //  animate
                $anim = $(e).animate({height: "show"}, options.speed)
                
                if(options.period && options.period > 0){
                    $anim.animate({opacity: 1.0}, options.period)
                        .animate({height: "hide"}, options.speed);
                }
            }
            else{
                $(e).animate({height: "hide"}, options.speed)
            }
            
            $(e).css("height",$.ui.notificationmsg._height);
        },
        fade: function(e, options) {
            if($(e).is(":hidden")){
                //  animate
                $anim = $(e).animate({opacity: "show"}, options.speed);
                
                if(options.period && options.period > 0){
                    $anim.animate({opacity: 1.0}, options.period)
                        .animate({opacity: "hide"}, options.speed);
                }
            }
            else{
                $(e).animate({opacity: "hide"}, options.speed);
            }
            
            $(e).css("opacity",1.0);
        },
        slidethru: function(e, options) {
            //  set the position and left
            var b = $.ui.notificationmsg._bottompost;
            var h = $.ui.notificationmsg._height;
            if($(e).is(":hidden")){
                //  animate
                $anim = $(e).animate({height: "show"}, options.speed);
                
                if(options.period && options.period > 0){                       
                    $anim.animate({opacity: 1.0}, options.period)
                        .animate({height: "hide", bottom: h}, options.speed)
                        .animate({bottom: b}, 1);
                }
            }
            else{
                $(e).css({height:h,bottom:b});
                $(e).animate({height: "hide", bottom: h}, options.speed)
                    .animate({bottom: b}, 1);
            }
            $(e).css({height:h,bottom:b});
                           
        }
    }
});
})(jQuery);