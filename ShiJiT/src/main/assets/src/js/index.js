var bgObj = {};
// $(document).on('scroll',function(){
//     var scrollTop = $(document).scrollTop();
//     if($(document).scrollTop() > 375){
//         bgObj.bgCss = {
//             'background':'transparent',
//             'background-size' : '100% auto',
//             'background-attachment' : 'fixed'
//         }
//     }else{
//         bgObj.bgCss = {
//             'background':'url(' + bgObj.bgUrl + ') no-repeat 0 ' + bgObj.pos + 'px',
//             'background-size' : '100% auto',
//             'background-attachment' : 'fixed'
//         }
//     }
//     $('body').css(bgObj.bgCss);
// });
function goTop(){
    document.getElementsByTagName('body')[0].scrollTop = 0;
};
//文档加载完成
$(function(){
function rotate(){
	$('.detail-rotate').css({'border-left-width': (screenWidth) + 'px'});
}
rotate();
})
