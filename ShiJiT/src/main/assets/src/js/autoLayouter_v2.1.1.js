/**
* Plug-in name: autoLayouter.js v2.1.1
* Description: Detail page auto layout for texts and images
* Create date: 2015-03-18
* Author: jinguang@iyuntoo.com
* Notice: for mobile webapp instead of jQuery used zepto, 
*         maybe this cause sometimes $obj.css('left') returning 'auto',
*         so have to use $obj[0].offsetLeft, this will return '0' int.
*/
var autoLayouter = {
	isNewRow: true,
	screenw: document.documentElement.clientWidth || document.body.clientWidth,
	screenh: 0,
	spacew: Math.ceil(0.0406*document.documentElement.clientWidth), //图片和图片或者文字和图片之间的距离
	marginw: 0, //左侧边界与图片距离
	tempArray: [],
	titleImageView: {},
	keepcorrect :  Math.ceil(0.0406*document.documentElement.clientWidth)/2,  //垂直方向高度容错
	exe: function($titleImage, currentView, viewsArray) { // $titleImage: a story first image; currentView: the zooming or dragging view; viewsArray: all views except the story title
		this.setTitleImage($titleImage);
		for(var i = 0; i < viewsArray.length; i++) {
			var $current = $(viewsArray[i]); // newRect
			if(!this.isNewRow) {
				if(this.tempArray.length == 1) {
					var $temp = this.tempArray[this.tempArray.length - 1];
					if(Math.floor(parseInt($temp[0].offsetLeft) + parseInt($temp.width()) + autoLayouter.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
						$current.css('left', parseInt($temp[0].offsetLeft) + parseInt($temp.width()) + this.spacew);
						$current.css('top', parseInt($temp.css('top')));
						this.tempArray.push($current);
					} else {
						this.isNewRow = true;
					}
				}
				else if(this.tempArray.length == 2) {
					var $temp = this.tempArray[this.tempArray.length - 1];
					if(Math.floor(parseInt($temp[0].offsetLeft) + parseInt($temp.width()) + this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
						$current.css('left', parseInt($temp[0].offsetLeft) + parseInt($temp.width()) + this.spacew);
						$current.css('top', parseInt($temp.css('top')));
						this.tempArray.push($current);
					} else {
						var first = this.tempArray[0],
							last  = this.tempArray[this.tempArray.length - 1];
						if(Math.floor(parseInt(first.css('top')) + parseInt(first.height())) < Math.ceil(parseInt(last.css('top')) + parseInt(last.height())) + this.keepcorrect) {
							if(Math.floor(this.marginw + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(last[0].offsetLeft))) {
								$current.css('left', this.marginw);
								$current.css('top', parseInt(first.css('top')) + parseInt(first.height()) + this.spacew);
								if(parseInt($current.css('left')) + parseInt($current.width()) + this.spacew < parseInt(last[0].offsetLeft)){
									this.tempArray.splice(0, 0, $current); // insert
								}else{
									this.tempArray.splice(0, 1, $current); // replace
								}
							} else {
								this.isNewRow = true;
							}
						}
						else if(Math.ceil(parseInt(first.css('top')) + parseInt(first.height()) + this.keepcorrect) > Math.floor(parseInt(last.css('top')) + parseInt(last.height()))) {
							if(Math.floor(parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
								$current.css('left', parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew);
								$current.css('top', parseInt(last.css('top')) + parseInt(last.height()) + this.spacew);
								if(parseInt($current[0].offsetLeft) + parseInt($current.width()) + this.spacew < this.screenw - this.marginw)
									this.tempArray.splice(1, 0, $current); // insert
								else
									this.tempArray.splice(1, 1, $current); // replace
							} else {
								this.isNewRow = true;
							}
						}
						else {
							this.isNewRow = true;
						}
					}
				}
				else if(this.tempArray.length == 3) {
					var first  = this.tempArray[0],
						mid    = this.tempArray[1],
						last   = this.tempArray[2],
						yFirst = parseInt(first.css('top')) + parseInt(first.height()) + this.spacew,
						yMid   = parseInt(mid.css('top')) + parseInt(mid.height()) + this.spacew,
						yLast  = parseInt(last.css('top')) + parseInt(last.height()) + this.spacew;
					if(yFirst < yMid) {
						if(yMid <= yLast) {
							if(Math.floor(this.marginw + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(mid[0].offsetLeft))) {
								$current.css('left', this.marginw);
								$current.css('top', parseInt(first.css('top')) + parseInt(first.height()) + this.spacew);
								this.tempArray.splice(0, 1, $current); // replace
							}
							else if(Math.floor(this.marginw + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(last[0].offsetLeft))) {
								$current.css('left', this.marginw);
								$current.css('top', parseInt(mid.css('top')) + parseInt(mid.height()) + this.spacew);
								this.tempArray.shift(); // remove
								this.tempArray.splice(0, 1, $current); // replace
							}
							else {
								this.isNewRow = true;
							}
						}
						else if(yFirst <= yLast && yLast < yMid) {
							if(Math.floor(this.marginw + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(mid[0].offsetLeft))) {
								$current.css('left', this.marginw);
								$current.css('top', parseInt(first.css('top')) + parseInt(first.height()) + this.spacew);
								this.tempArray.splice(0, 1, $current); // replace
							}
							else if(Math.floor(parseInt(mid[0].offsetLeft) + parseInt(mid.width()) + this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
								$current.css('left', parseInt(mid[0].offsetLeft) + parseInt(mid.width()) + this.spacew);
								$current.css('top', parseInt(last.css('top')) + parseInt(last.height()) + this.spacew);
								this.tempArray.splice(2, 1, $current); // replace
							}
							else {
								this.isNewRow = true;
							}
						}
						else if(yLast < yFirst) {
							if(Math.floor(parseInt(mid[0].offsetLeft) + parseInt(mid.width()) + this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
								$current.css('left', parseInt(mid[0].offsetLeft) + parseInt(mid.width()) + this.spacew);
								$current.css('top', parseInt(last.css('top')) + parseInt(last.height()) + this.spacew);
								this.tempArray.splice(2, 1, $current); // replace
							}
							else if(Math.floor(this.marginw + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(mid[0].offsetLeft))) {
								$current.css('left', this.marginw);
								$current.css('top', parseInt(first.css('top')) + parseInt(first.height()) + this.spacew);
								this.tempArray.splice(0, 1, $current);
							}
							else {
								this.isNewRow = true;
							}
						}
					}
					else if(yFirst > yMid) {
						if(yMid > yLast) {
							if(Math.floor(parseInt(mid[0].offsetLeft) + parseInt(mid.width())+ this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
								$current.css('left', parseInt(mid[0].offsetLeft) + parseInt(mid.width()) + this.spacew);
								$current.css('top', parseInt(last.css('top')) + parseInt(last.height()) + this.spacew);
								this.tempArray.splice(2, 1, $current);
							}
							else if(Math.floor(parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
								$current.css('left', parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew);
								$current.css('top', parseInt(mid.css('top')) + parseInt(mid.height()) + this.spacew);
								this.tempArray.splice(1, 1); // remove
								this.tempArray.splice(1, 1, $current); // replace
							}
							else {
								this.isNewRow = true;
							}
						}
						else if(yFirst > yLast && yLast >= yMid) {
							if(Math.floor(parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(last[0].offsetLeft))) {
								$current.css('left', parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew);
								$current.css('top', parseInt(mid.css('top')) + parseInt(mid.height()) + this.spacew);
								this.tempArray.splice(1, 1, $current);
							}
							else if(Math.floor(parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
								$current.css('left', parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew);
								$current.css('top', parseInt(last.css('top')) + parseInt(last.height()) + this.spacew);
								this.tempArray.splice(1, 1); // remove
								this.tempArray.splice(1, 1, $current); // replace
							}
							else {
								this.isNewRow = true;
							}
						}
						else if(yLast >= yFirst) {
							if(Math.floor(parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(last[0].offsetLeft))) {
								$current.css('left', parseInt(first[0].offsetLeft) + parseInt(first.width()) + this.spacew);
								$current.css('top', parseInt(mid.css('top')) + parseInt(mid.height()) + this.spacew);
								this.tempArray.splice(1, 1, $current); // replace
							}
							else if(Math.floor(this.marginw + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(last[0].offsetLeft))) {
								$current.css('left', this.marginw);
								$current.css('top', parseInt(first.css('top')) + parseInt(first.height()) + this.spacew);
								this.tempArray.splice(0, 1); // remove
								this.tempArray.splice(0, 1, $current); // replace
							}
							else {
								this.isNewRow = true;
							}
						}
					}
					else {
						if(yMid < yLast) {
							if(Math.floor(this.marginw + parseInt($current.width()) + this.spacew) <= Math.ceil(parseInt(last[0].offsetLeft))) {
								$current.css('left', this.marginw);
								$current.css('top', parseInt(first.css('top')) + parseInt(first.height()) + this.spacew);
								this.tempArray.splice(0, 1, $current); // replace
							} else {
								this.isNewRow = true;
							}
						}
						else if(yMid > yLast) {
							if(Math.floor(parseInt(mid[0].offsetLeft) + parseInt(mid.width()) + this.spacew + parseInt($current.width())) <= Math.ceil(this.screenw - this.marginw)) {
								$current.css('left', parseInt(mid[0].offsetLeft) + parseInt(mid.width()) + this.spacew);
								$current.css('top', parseInt(last.css('top')) + parseInt(last.height()) + this.spacew);
								this.tempArray.splice(2, 1, $current); // replace
							} else {
								this.isNewRow = true;
							}
						}
						else {
							this.isNewRow = true;
						}
					}
				}
			}
			if(this.isNewRow) {
				this.isNewRow = false;
				var y = 0;
				$.each(autoLayouter.tempArray, function(k, v) {
					if(y < parseInt(v[0].offsetTop) + parseInt(v.height()) + autoLayouter.spacew) {
						y = parseInt(v[0].offsetTop) + parseInt(v.height()) + autoLayouter.spacew;
					}
				});
				if(y == 0) { // title is fixed at the top, so y == 0 means title image, and this if means now zooming the title image, so it is used for title image shadow
					if($(currentView).hasClass('cover') && this.titleImageView.hasClass('cover')) // if(currentView == this.titleImageView)
						y = parseInt(this.titleImageView[0].offsetTop);
					else
						y = parseInt(this.titleImageView[0].offsetTop) + parseInt(this.titleImageView.height()) + this.spacew;
					y = 0;
				}
				$current.css('left', this.marginw);
				$current.css('top', y);
				this.tempArray = [];
				this.tempArray.push($current);
			}
		} // for end
		this.getHeight();
		return this.screenh;
	},
	setTitleImage: function($titleImage) { // when call exe function need to support a $titleImage object
		this.titleImageView = $titleImage;
	},
	getHeight: function() { // get height after exe insider for loop done
		var tmpArr = [];
		this.screenh = 0;
		for(var i = 0; i < this.tempArray.length; i++) {
			tmpArr.push(parseInt(this.tempArray[i].css('top')) + parseInt(this.tempArray[i].height()));
		}
		tmpArr = tmpArr.sort(function(v1, v2) {
			return v1 - v2;
		});
		this.screenh = tmpArr[tmpArr.length - 1];
	},
	resetting: function() {
		this.isNewRow = true,
		this.screenw = document.documentElement.clientWidth || document.body.clientWidth,
		this.screenh = 0,
		this.spacew = Math.ceil(0.0406*document.documentElement.clientWidth), //图片和图片或者文字和图片之间的距离
		this.marginw = 0, //左侧边界与图片距离
		this.tempArray = [],
		this.titleImageView = {},
		this.keepcorrect =  Math.ceil(0.0406*document.documentElement.clientWidth)/2  //垂直方向高度容错
	}
}
