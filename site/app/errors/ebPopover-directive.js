/*
Copyright © 2015 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
/**
 * Created by Anthony Salembier and Jason Conard on 17/07/15.
 */
angular.module('myApp.errors.ebPopover-directive', [])

    .directive('ebPopover', ['$timeout', function($timeout) {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
      var jqElement = $(element);
      var popoverElem = $('#eb-popover');
      var popoverTriangleElem = $('#eb-popover-triangle');

      if(popoverElem.length === 0) {
        $('body').append('<div id="eb-popover"><span id="eb-popover-span"></span><div id="eb-popover-triangle"></div></div>');
        popoverElem = $('#eb-popover');
        popoverTriangleElem = $('#eb-popover-triangle');
      }

      popoverElem.css({
        'z-index' : '100000',
        'display' : 'block',
        'position' : 'fixed',
        'top' : '-250px',
        'left' : '-250px',
        'padding' : '10px 20px',
        'color' : 'white',
        'background' : 'rgba(85,85,85,1)',
        'max-width' : '150px',
        'pointer-events' : 'none',
        'text-align' : 'center',
        'font-size' : '12px',
        'border-radius' : '3px',
        '-webkit-transition': 'opacity 200ms ease-out',
        '-moz-transition': 'opacity 200ms ease-out',
        '-ms-transition': 'opacity 200ms ease-out',
        '-o-transition': 'opacity 200ms ease-out',
        'transition': 'opacity 200ms ease-out'
      });


      popoverTriangleElem.css({
        'position' : 'absolute',
        'width' : '0',
        'height' : '0',
        'border-style': 'solid'
      });


      var refreshPopover = function(){
        var position = attrs['ebPopoverPosition'];
        var content = attrs['ebPopoverContent'];


        $('#eb-popover-span').html(content).toString();

        var offset = jqElement.offset();
        var width = 190;
        var height = 0;
        var elementHeight = 0;
        var elementWidth = 0;

        // Waiting for the text loading in popover
        $timeout(function(){

          if(position === 'left') {

            height = popoverElem[0].offsetHeight / 2;
            elementHeight = jqElement[0].offsetHeight / 2;

            width = popoverElem[0].offsetWidth + 30;

            popoverElem.css({
              'top' : offset.top - height + elementHeight + 'px',
              'left' : offset.left - width + 'px',
              'opacity' : 1
            });

            popoverTriangleElem.css({
              'left' : 'auto',
              'right' : '-20px',
              'top' : height - 15 + 'px',
              'border-width': '15px 0 15px 20px',
              'border-color': 'transparent transparent transparent rgba(85,85,85,0.8)'
            });


          } else if (position === 'right') {

            height = popoverElem[0].offsetHeight / 2;
            elementHeight = jqElement[0].offsetHeight / 2;
            width = popoverElem[0].offsetWidth;
            elementWidth = jqElement[0].offsetWidth;

            popoverElem.css({
              'top' : offset.top - height + elementHeight + 'px',
              'left' : offset.left + elementWidth + 30 + 'px',
              'opacity' : 1
            });

            popoverTriangleElem.css({
              'left' : '-20px',
              'right' : 'auto',
              'top' : height - 15 + 'px',
              'border-width': '15px 20px 15px 0',
              'border-color': 'transparent rgba(85,85,85,0.8) transparent transparent'
            });



          } else if (position === 'top') {

            height = popoverElem[0].offsetHeight + 30;
            elementHeight = jqElement[0].offsetHeight;
            width = popoverElem[0].offsetWidth / 2;
            elementWidth = jqElement[0].offsetWidth / 2;

            popoverElem.css({
              'left' : offset.left - width + elementWidth + 'px',
              'top' : offset.top - height + 'px',
              'opacity' : 1
            });

            popoverTriangleElem.css({
              'left' : width - 15 + 'px',
              'right' : 'auto',
              'top' : 'auto',
              'bottom' : '-20px',
              'border-width': '20px 15px 0 15px',
              'border-color': 'rgba(85,85,85,0.8) transparent transparent transparent'
            });

          } else if (position === 'bottom') {
            height = popoverElem[0].offsetHeight;
            elementHeight = jqElement[0].offsetHeight;
            width = popoverElem[0].offsetWidth / 2;
            elementWidth = jqElement[0].offsetWidth / 2;

            popoverElem.css({
              'left' : offset.left - width + elementWidth + 'px',
              'top' : offset.top + elementHeight + 30 + 'px',
              'opacity' : 1
            });

            popoverTriangleElem.css({
              'left' : width - 15 + 'px',
              'right' : 'auto',
              'top' : '-20px',
              'bottom' : 'auto',
              'border-width': '0 15px 20px 15px',
              'border-color': 'transparent transparent rgba(85,85,85,0.8) transparent'
            });
          }
        });
      };


      element.on('mouseenter', function() {
        refreshPopover();
      });

      element.on('mouseleave', function(){
        popoverElem.css({
          'opacity' : '0'
        });
      });

      scope.$watch('mapManager.currentMode', function(){
        popoverElem.css({
          'opacity' : '0'
        });
      });

    }
  }
}]);
