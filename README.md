# plugin-douyin
基于抖音的Xposed插件

### 执行动作的数据格式


####	//点赞

    {
	"videoId":"6693124310501969166"  //视频ID
	}
	
  
####	//评论

	{
	"videoId":"6693124310501969166",  //视频ID
	"commentText":"这个视频真赞呦!"   //评论内容
	}
  
  
####	//发送图片

	{
    "toUID":"3768748035023923",  //目标用户ID
    "imagePath":"/storage/emulated/0/FreeVideo/FreeVideo_share_image.jpg"   //图片路径
	}
  
  
#### //发送分享视频
	{
    "toUID":"3768748035023923",    //目标用户ID
    "formUID":"1147517703031011",  //视频来源用户ID
    "videoID":"6693124310501969166",  //视频ID
    "title":"分享视频",  //分享标题
    "coverUri":"17b4b00084db599b06eaa",  //封面uri
    "coverUrl":"https://p3-dy.byteimg.com/aweme/300x400/17b4b00084db599b06eaa.jpeg"
	}
  
  
####	//发送分享商品

	{
    "toUID":"3768748035023923",    //目标用户ID
    "formUID":"2343801518304232",  //商品来源用户ID
    "avatarUri":"temai/Fsn1xyep1a-udZWf2jSIh5btec2swww800-800",
    "avatarUrl":"http://p1.pstatp.com/aweme/720x720/temai/Fsn1xyep1a-udZWf2jSIh5btec2swww800-800.jpeg",
    "title":"旅行洗漱杯多功能刷牙杯子牙具盒漱口杯套装牙刷收纳盒便携式",  //标题
    "promotionId":"3356738809466489345",  //促销ID
    "productId":"3356713428592002459",  //商品ID
    "userCount":554352  //多少人看过的字段
	}
  
  
####	//发送名片

	{
    "toUID":"3768748035023923",  //目标用户ID
    "formUID":"1147517703031011",  //名片用户ID
    "avatarUri":"93dd00183553d67c79ca",
    "avatarUrl":"https://p3-dy.byteimg.com/aweme/720x720/93dd00183553d67c79ca.jpeg",
    "name":"杭州之声",
    "desc":"fm898989"
	}
  
  
#### 注意：需要在每个JSON里面添加对象的action;
| 动作        | 对应action   |
| --------   | -----:  |
| 点赞      | doFeed   |
| 评论        |   doComment   |
| 发送图片        |    img    |
| 发送视频      | video   |
| 发送商品        |   good   |
| 发送名片        |    card    |
        
	
	
#### 获取用户列表：
    粘贴用户分享链接到输入框，点击加载，等待加载完成后数据列表会自动填充到下发list;加载过程可能有些长。
    例子：https://www.iesdouyin.com/share/user/109864890252
