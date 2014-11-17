aixuexiao
=========

一个基于SAE Java平台的微信平台公众帐号应用例子。

详情访问：http://aixuexiao.sinaapp.com
微信公众帐号：爱学校(aixuexiao)


微信公众帐号功能为作为学生家长和学校老师之间的沟通桥梁。
学生家长可通过和微信公众帐号交互来获取学生在校的考试情况／班级动态／老师留言等信息。
当然需要学校老师在微信公众帐号后台添加学生留言／班级动态／考试信息等数据。

应用基于SAE Java平台开发，使用（Spring/SpringMVC/Mybatis）框架开发，有相同需求（在SAE Java平台搭建微信公众帐号后台）的同学可用拿去作为参考，当然也可用作为在SAE Java平台中使用框架的例子程序作为参考。

微信的验证和回复的入口程序在com.aixuexiao.web.controller.WeixinController.java中。
其中initWeixinURL（对应URL:/weixin GET）方法为验证方法。
replyMessage（对应URL:/weixin POST）方法则是回复方法。

直接下载本项目在SAE Java平台上搭建需要改动地方：
1.在你创建SAE应用的MySQL中执行应用根目录下的app_aixuexiao.sql文件（包括表结构和部分测试数据）；
2.修改src下db.properties文件中的数据库信息ak、sk分别设置为你SAE应用中的ak和sk即可。


注：时间仓促且第一次使用SpringMVC，不优雅的代码敬请指出。

有任何问题可用联系我，看到第一时间回复

URL:http://1.huyue520.sinaapp.com/weixin
Token :huyue520
EncodingAESKey:QQOcFIt5R0LfZNQUc6DHkH4a5UDZWYqoaepdj9xdNd2

/weixin?signature=a7a5bc62c8ca96aac10c9864f18905882d10cdea&echostr=3565340920539512763&timestamp=1416194744&nonce=1997749682 500 5000 - - 

weixin?signature=c4b3051b6189f643d50123d49dfae9a2a7cb07da&echostr=6286432969040846507&timestamp=1416206371&nonce=533799762
