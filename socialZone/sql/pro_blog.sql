/*
 Navicat Premium Data Transfer

 Source Server         : localhost57
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : localhost:3306
 Source Schema         : pro_blog

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : 65001

 Date: 18/09/2025 16:33:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '分类id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面',
  `video` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `visibility` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'public' COMMENT '可见性: public-全部可见, friends-好友可见, followers-关注我的可见, private-仅自己可见',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '博客管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (1, '不懂就问 这蛋是啥情况啊！！！', 8, 1, '2025-08-10 13:36:15', 'http://127.0.0.1:9090/web/download/ea7a44f10e9b417f8d2256e2947e03c2.png', 'http://127.0.0.1:9090/web/download/079e49a6681945d199a59f3645f1b5dc.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">第一次看见这样的蛋，是小鸡要孵出来然后被煮了吗</span><img src=\"https://picasso-static.xiaohongshu.com/fe-platform/14b005f7afd5f7c88620478b610bf1de90c4ceab.png\" alt=\"\" data-href=\"\" style=\"height: 16px;\"/><img src=\"https://picasso-static.xiaohongshu.com/fe-platform/14b005f7afd5f7c88620478b610bf1de90c4ceab.png\" alt=\"\" data-href=\"\" style=\"height: 16px;\"/><img src=\"https://picasso-static.xiaohongshu.com/fe-platform/14b005f7afd5f7c88620478b610bf1de90c4ceab.png\" alt=\"\" data-href=\"\" style=\"height: 16px;\"/><img src=\"https://picasso-static.xiaohongshu.com/fe-platform/14b005f7afd5f7c88620478b610bf1de90c4ceab.png\" alt=\"\" data-href=\"\" style=\"height: 16px;\"/><img src=\"https://picasso-static.xiaohongshu.com/fe-platform/14b005f7afd5f7c88620478b610bf1de90c4ceab.png\" alt=\"\" data-href=\"\" style=\"height: 16px;\"/><img src=\"https://picasso-static.xiaohongshu.com/fe-platform/14b005f7afd5f7c88620478b610bf1de90c4ceab.png\" alt=\"\" data-href=\"\" style=\"height: 16px;\"/><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">一瞬间食欲全无了</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (2, '你可以永远相信来自意大利的冰淇淋', 3, 1, '2025-08-06 15:38:42', 'http://127.0.0.1:9090/web/download/856dca0e17c748cca38921868949cf7f.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">真的很好吃！很像新鲜的gelato了！里面是酸甜的树莓酱+奶味非常浓郁（超过mackie）+芝士蛋糕饼底碎，整体口感丝滑也不甜腻 抓紧时间去lidl买！不知道下周还会不会有了😭</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (3, '这样的葡萄谁看了不买！', 3, 1, '2025-08-06 15:39:35', 'http://127.0.0.1:9090/web/download/617c3322f8c44212a266f7054afab374.png', 'http://127.0.0.1:9090/web/download/c9de02d3e9a74bf888c033eb91850a57.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">小时候的葡萄味，酸酸甜甜，一看到这种葡萄就想买！😘</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (4, '为啥我的干蒸牛肉会出很多水', 3, 1, '2025-08-06 15:40:53', 'http://127.0.0.1:9090/web/download/22009c5384b746a6a6f766ee0f1fa7c1.png', 'http://127.0.0.1:9090/web/download/b14b5764d7314c6d926cb1de0314aa3e.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">是因为我蒸的时间太短没有把产生出的水分蒸干吗</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (5, '为什么现在大家化妆都很少画眼线了？', 4, 2, '2025-08-06 15:41:43', 'http://127.0.0.1:9090/web/download/161297513cb7486884263871c932620b.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">难道是我的错觉吗？为什么感觉大家现在化妆很少画眼线了？朋友说她感觉画眼线压眼睛，显得眼睛更小了？但是画眼线不是可以让眼睛变得更大一些吗，怎么会显得眼睛更小呢，我有点get不到。。。</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (6, '这到底是做啥用的', 4, 2, '2025-08-06 15:42:58', 'http://127.0.0.1:9090/web/download/54ed33bd816a473c8b95ae35f117c6e2.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">一直记得某一年买化妆品有个赠品是这样的东西(其实我也不知道是什么😬） 想找出来搅和粉底液用 找呀找呀就是找不到 最后在我初中的笔袋里找到了这一个 感谢我初中的时候总买点乱七八糟的东西 当时应该是准备拿来当镜子上学带着去臭美的😂不过那时候买过好几个 现在全都找不着了 隐约记得这个特别容易划花 还有我初中笔袋的样子 到现在也觉得好看 果然我的审美十几年没有什么变化 感谢网络闺蜜们，帮我确定了这个就是小镜子也确定了这一个是买化妆品送的，我自己买的那些全都找不到了我这个撕了膜了，就是这样模糊不清楚，所以我才一直不确定它到底是做什么用的</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (7, '一个疑惑：你们考研期间都化什么妆啊？', 4, 2, '2025-08-06 15:43:47', 'http://127.0.0.1:9090/web/download/ff48a3e78d384ffbb9504c6e08b2c550.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我感觉我自己学习学傻了，每天泡在当地图书馆里不能自拔，对着镜子里的自己看呆呆的，脸上没有一点起色，想早上早起化个精致的妆容改善下自己，可还要早起抢图书馆的座位😭考研搭子一直劝我，姐妹们，谁能给我个妆容参考下，感激不尽~</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (8, '替领导开会签完到提前跑了', 7, 3, '2025-08-06 15:46:34', 'http://127.0.0.1:9090/web/download/9328b1e595ae4b678fbefd0a8029013e.jpg', '', '图片', '<p><span style=\"font-size: 16px;\">替领导开会签完到提前跑了，结果没想到，人家还签退，领导被全县通报了 后续：今天领导见到我说，小萝啊，还要情况说明，我乖乖的写了情况说明给他审阅，这件事应该就这么过去了 没想到这个帖子火了，大家的祸都闯的五花八门，后续就是我给他写了情况说明，一会儿再给他送到县里去，他其实是有别的会冲突了，就派我去了个以为不太重要的视频会，他没想到我早跑，我也没想到会签退</span><span style=\"color: rgba(51, 51, 51, 0.6); font-size: 14px;\"><br></span></p>', 0, 'public');
INSERT INTO `blog` VALUES (9, '叠纸游戏2026校园招聘正式启动！', 7, 3, '2025-08-06 15:47:21', 'http://127.0.0.1:9090/web/download/56b4a089d7784094b4f4a5ddf72d81ef.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">🌟面向对象：2026届应届毕业生（毕业时间在2025.09-2026.08） 🌟网申时间：8月1日-10月31日 🌟岗位类别：程序技术类、美术表现类、产品策划类、市场运营类、IP开发类、质量管理类、游戏音频类、职能支持类</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (10, '求助 开车45分钟通勤的班要去吗？', 7, 3, '2025-08-06 15:49:52', 'http://127.0.0.1:9090/web/download/ecf91ead89c94824adabbcbf9d145343.webp', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">如题，找到了个双休的工作，但是有点偏僻，坐车根本到不了，准备开车上班，8.30上班，通勤45分钟 我自己对于开车上班还有新鲜感，但是不知道会不会后面就很疲惫，求告知。</span></p><p><br></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">大家的回复我都有看到，上班通勤真的是大家共同的殇，坚持那么久的都是超级厉害的人呀，太长通勤的也要照顾好自己哦，不要太辛苦了。 回到我本身，这份工作出现的时间太巧了，双休，工资也合适，听招聘方说工作量也不大，冬天还会延迟半小时上班。 对开车上班的新鲜感还在，以前在外地试过地铁公交单车，让我试试看开车上班吧！</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (11, '追星女就不可能辞职', 7, 3, '2025-08-06 15:51:39', 'http://127.0.0.1:9090/web/download/c3e0685e99ca42bf9e803d0f908c5ea8.png', 'http://127.0.0.1:9090/web/download/85322099df9443f88a6e0cb5c407453d.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">附了原图，欢迎大家交作业 ps 上班时间完成，用的是公司的打印机，公司的剪刀，公司的创可贴卷一卷贴上的</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (12, '好像不小心和侠缘说漏嘴了', 6, 3, '2025-08-06 15:53:24', 'http://127.0.0.1:9090/web/download/47f33988566548ef9c2860f83b87d1f0.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">晚上和侠缘一起觉的时候 我们再次聊到了结缘的话题 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我依旧坚决的表示：我以后不进醉花阴</span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\"> 我侠缘则仍然回复那句反驳：那你以后碰到更喜欢的人了怎么办？ </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我：我只喜欢你 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我侠缘：你要是突然喜欢上别人了呢？ </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我：那就死 一系列重复的话术拉扯之后 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我脑海忽然闪过一句话 我：你是不是喜欢上别人了？ </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我侠缘一直没接话 此时已经凌晨一点多 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我不知道她是睡着了 还是沉默了 我不知道…</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (13, '从开口就冷场到句句点头，表达力让我翻盘', 8, 2, '2025-08-06 15:56:50', 'http://127.0.0.1:9090/web/download/bba481867b4b4edebd2bcbdf27917388.png', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">心中有模板，脑中有框架，只要发言，张口就来 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">每天10分钟练口才，63天之后你会惊艳会所有人 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">巴菲特说:要学会演讲。这是一项可以持续使用五六十年的资产，如果你不喜欢演讲，就要承受五六十年的损失。 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">卡耐基说:一个人的成功85%靠他的人际沟通和演说能力，只有15%靠他的专业技能 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">马云说:如果你不会说话，你的人生就是十分耕耘一分收获。如果你学会说话，就是一分耕耘十分收获。 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">平时讲话自然流畅，一到台上就慌了神，一紧张就掉链子，怎么办？ </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">来学表达，学会了如同安上了一双翅搒，任何地方不怯场，自信、又把自己的观点通过表达种在别人心中，生根发芽。 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">学会当众讲话，提升表达影响力! </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">只要你敢于发出声音，就会有观众和粉丝，有追随者!加入表达创富营改变从此刻发生...</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (14, 'crush深夜在朋友圈发美照吸引男人如何调理', 8, 2, '2025-08-06 15:57:59', 'http://127.0.0.1:9090/web/download/2551ee0f81c047c0aa99a9f193b2de6c.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">本人直女，在遇到这样的事情之前也没想到过人生竟然能如此戏剧化。 前期提要，本人小时候长时间患有一种影响外貌的疾病因此很一直很内向，也有点自卑，没怎么接触过异性。直到长大一些彻底摆脱了这种病，从此以后性情大变，成为了一种神奇的厚脸皮体质。有次在某个旅游景区打黑车的时候，一起拼车的帅哥真的是一眼踩在了小女子审美点上，白白净净的小脸加一副斯斯文文的眼镜，抓得恰到好处的微分碎盖配合眼角的泪痣，1.85(目测）的完美身高更是馋得小女子直呼快哉快哉，于是果断要到了帅哥微叉，旅游结束以后我们开始在网上热聊。 聊天以后发现他并非徒有其表，他很会聊天，风趣幽默，每次跟他聊天的愉快程度让我不禁怀疑这是不是老天专门针对我下的杀猪盘，但是在此等尤物的强烈吸引力面前，相信没有谁能抵制住这种诱惑，于是一场针对我的巨大服从性测试由此拉开了帷幕。 鄙人平时很喜欢视奸他的朋友圈，因为这位小帅哥深谙朋友圈美学，审美不错，拍出来的照片赏心悦目，多看两眼咱大女人才有力气讨生活，并且还十分情感丰富，生活中遇到的一些琐事也能被他扩写成一篇文笔细腻的小作文。由此引出了关键的部分，在一个夜深人静的夜晚，小女子朋友圈刷新出来一条十分吸引眼球的他的九宫格美照，看得人目眦欲裂，目瞪口呆，每一张都是极其美艳，直男根本无法想象的拍照姿势和角度，其娇媚程度令人叹服，配合着一段钓系情感文案：这个世界欠调教，我也是。此时再点进他的朋友圈(可能是手滑不小心给我分组分错了)，里面记录了他为各任前男友写的小作文，比如其中有一任是他的金主爸爸，帅气多金的老男人，但对他只有生理需求毫无情感；还有一任是顶帅情绪价值拉满的男大，但是玩的花在全国各地范围内均有男女老少形形色色的对象。 看完以后我五感尽失，就这么茫茫然地沉沉睡去了，第二天早上起来的时候发现他所有朋友圈我都看不到了。在下觉得他应该是发现给我分组分错了，于是单删了我，但是至今不敢去给他发消息验证。由此达成了隐藏结局——湾仔码头。</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (15, '杭州花店门面景观改造', 9, 1, '2025-08-06 15:59:13', 'http://127.0.0.1:9090/web/download/0021f224433142f2b5b52f6e33b9f309.jpg', '', '图片', '<p>改造中</p>', 0, 'public');
INSERT INTO `blog` VALUES (16, '4.30号现版本唯一完美阵容，学会轻松王者！', 6, 4, '2025-08-06 16:00:01', 'http://127.0.0.1:9090/web/download/fb55cf64a62d461b84561925e70d087a.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">🔥阵容优点：现版本唯一完美阵容，过渡简单平滑，中期强势提速 🔥阵容分析：现版本卡莎在独家的情况下绝对可以说是最强阵容，中期强势提速，后期三星后可以打一切阵容，是当之无愧的版本之子。通常阵容都有缺点，而卡莎作为当前版本的完美阵容，唯一的缺点就是，怕同行，三星不了。 基础构架： 【二星守】芮尔 卡莎 【三未来】潘森 卢锡安 肾 搭配挂件（以下方案选择其一） 【搭挂件】费德提克 厄运小姐 莫甘娜 剑魔 【搭星守】艾克+辛德拉（推荐辛德拉来得早或有星守转） 【搭护卫】盖伦+瑞雯（推荐外面物理较多或有护卫转时） ❗在七人口的时候组成基础构架再搭配两张，有星守转给女枪，有护卫转给潘森 ❗七人口整体二星后看数量和同行情况选择追三星卡莎芮尔，还是选择上八提质量 🔥运营技巧： 开局抢攻速，优先做出核心装电刃，其次是鬼索狂暴之刃 【2阶段】什么二星上什么，优选三未来，护卫迅捷过渡 【3阶段】3-2升6级，小搜组成基础构架+1挂件，如质量较高，则继续过渡到4阶段 【4阶段】4-1升7搜出大部分二星，根据转职情况，选择基础构架搭配方案，无转职选则搭两挂件 【5阶段】根据卡莎数量决定追3星或升8补费德提克等强力挂件 【6阶段】追出三星卡莎再升9，通常情况下三星卡莎足以吃鸡 🔥装备方面： 卡莎： 必备（启动装二选一）：鬼索狂暴之刃/朔极之矛 备选：电刃，珠光护手，科技枪，帽子 芮尔：龙牙，反甲，板甲，狂徒 副C厄运小姐：朔极之矛，珠光护手，巨人杀手，帽子等法系输出装 满足上诉装备后，多余装备分配如下： 稻草人：离子火花，珠光护手，鬼书 优选英雄强化（强度按先后顺序）： 芮尔的坚守防线 卡莎的多重射击 其他体系内强化 最强强化：好事成双（两个挂件选择再上一个芮尔和卡莎） 优选强化： 星守转职，珠光莲花，护卫转职，迅捷射手之心 其他尽量选战力海克斯，如：狩猎律动，源计划甲壳 经济海克斯只选择dd街区 黄金门票 写在最后：整个s8赛季，其实卡莎是最强的阵容，没有之一，贯穿整个s8-s8.5，比贾克斯还要毒瘤的存在，但由于金铲铲卡莎一直有bug，所以卡莎一直是最弱的阵容。在卡莎修复bug后，终于在金铲铲迎来了曙光，乘着玩的人不多，快冲！</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (17, '期中考学生考太差不用上课了', 1, 4, '2025-08-06 16:01:25', 'http://127.0.0.1:9090/web/download/7825895032f14000985be2a0bd79937b.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">在机构兼职教初三物理 学生初二下学期期末考30多分 上课前老板跟我说他现在觉悟了，想好好学了 教了一个月（一星期一节课）后第一次月考48分，年级上进步了四五十名 但是分析试卷还是有好多重复讲过的题目做错了，把这些讲过做错了的分加上去可以考60，就定了60分为期中考目标 期中考前最后一次课他说这次考到欧姆定律前的知识，计算题考两道热学的。最后一节课就着重复习了热学部分（前几节课讲的电学） 最后期中考出来得了36分😢</span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">考完上课分析试卷他还沾沾自喜地说这个分数还进步了7名，把我都给气笑了 最后大题一道电学一道热学，基本没做出来 教的我也很心累，上课看似在认真的听，实则根本没有在动脑，重复讲了n多遍一会儿会一会儿不会。有时数学老师在隔壁给他上课，能听到数学老师被气得梆梆梆敲白板 本来是明早的课，突然说取消了，换老师了，虽然有点意外，但还是反思了一下自己教学中的不足 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">1.不成体系 物理教学时间不长，除了初三知识点，初二的遗忘较多。没有一个完整的上课体系，每次上课前都是先问学生想上哪部分，比较被动，知识东上一点西上一点，连贯性不足 2.不够严厉 基本没对学生发过火说过重话，有时学生状态不好（心不在焉）也只是温柔提醒一下，调整不过来也就不管顺着讲了 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">3.容易受学生情绪影响 另一个物理学生，从第一次上课就耷拉着脸，一脸不爽，挺聪明的学生，但上到后面就会不耐烦，做笔记什么的鬼画符，看到她的脸就很影响心情，后来也主动提出来不给她上课了 </span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">综上，虽然有一定问题，但教学过程中自己也做到认真负责，学生学不会真的很挫败啊 没事了，到月底彻底告别教培，跳出舒适圈</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (18, '不磨脚缎带', 2, 4, '2025-08-06 16:02:23', 'http://127.0.0.1:9090/web/download/7b74bc55c59441c78d97074b22701985.jpg', '', '图片', '<p>分享穿搭</p>', 0, 'public');
INSERT INTO `blog` VALUES (19, '下一个风口：“东方玄学+中药、水晶手串”贼猛', 2, 4, '2025-08-06 16:03:20', 'http://127.0.0.1:9090/web/download/c144c43180e84054a1ee92e2a576e8ef.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">在TikTok卖饰品，我靠“东方文化韵味”打了场翻身仗！爆了几千🥚！ 去年还在跟风卖流行款，卷到利润薄如纸，直到把普通白水手串包装成“月亮的眼泪·清透晶串”，配上东方chuan说故事和月光下的视频，三天卖光库存，才算摸到门道 中药手串+东方xuan学比水晶还猛！ 核心就是给饰品裹上“文化外衣”，这招让水晶、中药手串、银饰全爆单了！！ 具体怎么做给大家拆解一下 1️⃣水晶手串卖“自然意境”：改名“星辰守hu·白水串”，说它在东方文化里被视作自然纯粹的象征，能让人联想到澄澈的天地。视频拍模特在晨光中戴水晶做瑜伽，配风铃声，强调“天然矿物”“手工编织”，只提“营造宁静心境”，绝不说特殊作用 2️⃣中药手串卖“自然禅意”：檀香木串叫“沉境之息·冥xiang珠”，讲它在东方传统中常与静心时刻相伴，自带沉稳气息 视频拍熏香与旧书，模特戴串深呼吸，配颂钵音，突出“天然原木”“独特木纹”，只谈文化里的象征意义，不沾任何功效，但视频故事能让老外自己悟！ 3️⃣银饰卖“文化符号”：平安扣项链变“如意结·东方银饰”，说绳结是古老东方的吉祥符号，代表美好联结 视频拍老人编中国结、女孩戴链自信行走，强调“手工工艺”“传统符号现代演绎”，用“美好寓意”替代特殊说法 这套打法能成，关键在四点： 1️⃣老外对东方文化充满好奇，觉得有故事的东西够独特； 2️⃣卖的是情绪价值和文化体验，让饰品成为承载情感的物件 3️⃣天然、手工、独特性正对他们胃口 4️⃣精准打</span><a href=\"https://www.xiaohongshu.com/search_result?keyword=spirituality&amp;type=54&amp;source=web_note_detail_r10\" target=\"_blank\" style=\"text-align: start;\">#spirituality</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\"> </span><a href=\"https://www.xiaohongshu.com/search_result?keyword=easternwisdom%25E7%25AD%2589%25E6%25A0%2587%25E7%25AD%25BE%25E5%25BC%2595%25E6%25B5%2581&amp;type=54&amp;source=web_note_detail_r10\" target=\"_blank\" style=\"text-align: start;\">#easternwisdom等标签引流</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">，且严守红线不谈任何功效类表述 现在看啥饰品都能加东方元素：素圈戒指讲“圆满”的寓意，珍珠叫“月魄凝华”的雅称。用老外能理解的“东方文化”语言包装这些元素，在饰品赛道就是降维打击！！</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (20, '我好爱这个反派！', 5, 5, '2025-08-06 16:04:51', 'http://127.0.0.1:9090/web/download/d88a5910ce1149d38879f477577927aa.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我好爱模范出租车2里的这个反派角色，我觉得他长的好帅啊！</span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">因为被人误导做了坏事，最后我觉得不算洗白 如果他先遇到的是主角团，一定会变得很好吧</span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">就连最后流泪的时候都是在回忆出租车行的日子，他其实非常需要真诚的同伴和关爱啊</span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">第三季能不能把他复活，让他多做好事赎罪啊啊啊</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (21, '底特律一周目结局记录', 5, 5, '2025-08-06 16:05:40', 'http://127.0.0.1:9090/web/download/6b4553a90010436faf83dd9a160f092e.png', 'http://127.0.0.1:9090/web/download/8593646a020b4b019a65eb81f27ae8cb.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">马库斯领导革命胜利 Alex和卡拉存活 但是康纳中途就反库了</span></p><p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">感觉他那条线最精彩但是没打到</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (23, '汤姆猫的梦中情猫', 10, 5, '2025-08-06 16:08:26', 'http://127.0.0.1:9090/web/download/4cf1e9795dd046c197e37f981dc38526.png', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">一戴上蝴蝶结就鬼迷日眼的</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (25, '地铁工作人员 合肥', 7, 1, '2025-08-10 13:29:13', 'http://127.0.0.1:9090/web/download/523606e1f8ea409c9d0944084f288f6b.png', 'http://127.0.0.1:9090/web/download/ab6bcf2f771046a9a71a0e1a78c984a9.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">地铁工作人员有编制吗，坐标合肥，薪资怎么样</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (26, '差点把我看呕了，旅顺博物馆这两古代尸体', 5, 1, '2025-08-10 13:30:27', 'http://127.0.0.1:9090/web/download/55f3e9bb86ae45368c7e1cc7ea4dbe23.png', 'http://127.0.0.1:9090/web/download/94e65fe2f80a47dbbe2db89b4d6c2172.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">真的有点生理不适，皮肤都变成鱿鱼干了。</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (27, '抱歉，我家真的不接待剧组上门拍戏😅', 8, 1, '2025-08-10 13:31:25', 'http://127.0.0.1:9090/web/download/eebd24bf76e149f79e67d55fc8897c80.png', 'http://127.0.0.1:9090/web/download/eddeb897ad3243b3aff63d6195b8dbd6.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">我的农村自建房陆续收到了不少短剧剧组的邀约 剧组联系说房子有点老洋房的味道， 想要租用几天拍摄… 🖐🏻位数的场地费虽然很可观！ 但仔细考虑了一下还是拒绝了！ 毕竟房子是自己住， 运输摄影器材难免会有磕碰 i人还是自己安安静静宅着才舒服～</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (28, '南京博物院你真的很人性化', 8, 1, '2025-08-10 13:32:54', 'http://127.0.0.1:9090/web/download/8ef22a50a1754bfa9649a81d43b111e4.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">说实话，我逛了那么多博物馆，第一次在一个博物馆里看到了高峰期时，女生的专用卫生间，成功在3分钟之内上到了厕所，南博你好人性化</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (29, '武汉东湖磨山好美', 8, 1, '2025-08-10 13:33:45', 'http://127.0.0.1:9090/web/download/3fe79b5677fb4effb701bd0033c1b743.png', 'http://127.0.0.1:9090/web/download/bcd04decc87d4174956ce9cf482b5a58.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">武汉东湖磨山好美</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (30, '千万别入坑', 6, 1, '2025-08-10 13:38:54', 'http://127.0.0.1:9090/web/download/d2df9cd8d09644208e3ff7b8728bbf61.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">新买的华为平板，游戏党慎重入手，系统太封闭了。</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (31, '哇！台风来临前的家', 9, 1, '2025-08-10 13:40:38', 'http://127.0.0.1:9090/web/download/eaa28fce7f5f42b5a9e75c4b22bc46cc.png', 'http://127.0.0.1:9090/web/download/f4ee8a5011204ebeaecd848ce8c5230e.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">傍晚到家7点多 推开家门后真的被美到 台风前的天空 真的是七彩的 匆忙的拍了几张live 分享调色盘给大家🎨</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (32, '小猫的自我介绍', 10, 5, '2025-08-06 16:10:32', 'http://127.0.0.1:9090/web/download/cd0230509ceb456abf34cbaabd321770.png', 'http://127.0.0.1:9090/web/download/d57db38ada744f6385e270646bc6754b.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">这是来自小猫的自我介绍</span></p><p><br></p>', 0, 'public');
INSERT INTO `blog` VALUES (33, '是暑假带娃观影的最佳选择呀', 5, 5, '2025-08-06 16:06:30', 'http://127.0.0.1:9090/web/download/251d8e04ebbf42bab0192ba650551499.png', 'http://127.0.0.1:9090/web/download/fd1e8a18bc154b43b628d332c4c9e60d.mp4', '视频', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">大人儿童都能笑成一片， 只不过大人笑着笑着就沉默了， 因为真的很容易带入自身的现状， 那是因为真的很写实 （努力了好久还没上岸的样子， 面对最亲的人只能撒谎； 工作遇到困难， 面对父母只能报喜不报忧。） 从故事情节来说： 从我们熟知的故事出发， 除了有在西游世界的取经四人组， 还有另一个取经四人组。 让我们看到了西游故事的另一半。 从画面色调来说： 是真的对眼睛好啊， 草地、山间、乡村、水流等等 绿色调搭配真的好夏天 配色很美。 还有就是有的人物虽然只有寥寥几笔， 但是却能活灵活现体现出人物的灵魂。 看到结尾，突然想到取经路上的种种， 我们小时候看懂了吗？ 是我们知道的那样吗？ 大人物为了取得想要的效果， 是会为底层的草根的生存考虑的吗？ 最后引用片尾的一句话： “致我们都能活成自己想要的样子” 还有就是片中突然来一段陕北口音真的还蛮搞笑的哈哈哈哈哈哈哈哈哈😂</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (34, '一大早被巴黎警察这个纹身笑死了', 7, 1, '2025-08-10 13:35:05', 'http://127.0.0.1:9090/web/download/d351302e477b4111ba13e977d1a729ad.jpg', '', '图片', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px;\">纹身师傅也二， 顺序应该是 水木金火土 吧</span></p>', 0, 'public');
INSERT INTO `blog` VALUES (35, '风景真好', 5, 1, '2025-09-01 22:26:20', 'http://127.0.0.1:9090/web/download/808e9eab632047e9badfb40325f0ced4.jpg', '', '图片', '<p>太棒了</p>', 0, 'public');
INSERT INTO `blog` VALUES (37, '这个是好友才能看到的内容', 1, 1, '2025-09-02 09:00:31', 'http://127.0.0.1:9090/web/download/953d016baece4518adc24c99460cfe84.jpg', '', '图片', '<p>对对对</p>', 0, 'friends');
INSERT INTO `blog` VALUES (38, '这是一个关注我的可以看到的内容', 3, 1, '2025-09-02 09:19:40', 'http://127.0.0.1:9090/web/download/05d772b2e1c640a6b3cff6ac77926b44.webp', '', '图片', '<p>这是个关注我的信息</p>', 1, 'followers');
INSERT INTO `blog` VALUES (39, '这是个私有的内容', 1, 1, '2025-09-02 09:20:41', 'http://127.0.0.1:9090/web/download/4de08857d5ed49f0a62ae4c276e765e4.jpg', '', '图片', '<p>这是个私有内容 <img src=\"http://127.0.0.1:9090/web/download/eb5beb27a63c47c28e7a3671a5696d72.webp\" alt=\"\" data-href=\"\" style=\"\"/></p>', 0, 'private');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '收藏id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (6, 1, 19);
INSERT INTO `collect` VALUES (11, 1, 22);
INSERT INTO `collect` VALUES (3, 1, 24);
INSERT INTO `collect` VALUES (14, 2, 38);
INSERT INTO `collect` VALUES (7, 3, 23);
INSERT INTO `collect` VALUES (8, 4, 24);
INSERT INTO `collect` VALUES (2, 5, 24);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '评论人id',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评论时间',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父id',
  `origin_id` int(11) NULL DEFAULT NULL COMMENT '最上级评论id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '关联id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, '123', 1, '2025-08-06 05:29:06', NULL, NULL, 3);
INSERT INTO `comment` VALUES (2, '456', 1, '2025-08-06 05:29:10', NULL, 1, 3);
INSERT INTO `comment` VALUES (3, '123', 1, '2025-08-06 05:29:43', 2, 1, 3);
INSERT INTO `comment` VALUES (4, '456', 1, '2025-08-06 05:29:45', 2, 1, 3);
INSERT INTO `comment` VALUES (5, '可以', 1, '2025-08-06 05:55:24', NULL, NULL, 3);
INSERT INTO `comment` VALUES (7, '123', 1, '2025-08-06 06:11:08', NULL, 1, 3);
INSERT INTO `comment` VALUES (8, '123', 1, '2025-08-06 11:15:43', NULL, NULL, 29);
INSERT INTO `comment` VALUES (9, '可以留下你的**嘛', 1, '2025-08-06 11:35:54', NULL, NULL, 32);
INSERT INTO `comment` VALUES (10, '123123', 1, '2025-08-06 11:35:58', NULL, 9, 32);
INSERT INTO `comment` VALUES (11, '哇塞，你说的有点道理', 1, '2025-08-06 16:11:26', NULL, NULL, 19);
INSERT INTO `comment` VALUES (12, '猫儿这个魅惑哟，亲亲小猫', 3, '2025-08-06 16:12:02', NULL, NULL, 23);
INSERT INTO `comment` VALUES (13, '还好吧', 3, '2025-08-06 16:12:19', NULL, NULL, 10);
INSERT INTO `comment` VALUES (14, '小猫太可爱喽', 4, '2025-08-06 16:12:37', NULL, NULL, 24);
INSERT INTO `comment` VALUES (15, '教培就是很辛苦了', 4, '2025-08-06 16:12:52', NULL, NULL, 17);
INSERT INTO `comment` VALUES (16, '不明白', 4, '2025-08-06 16:13:00', NULL, NULL, 5);
INSERT INTO `comment` VALUES (17, '听起来很好看呢', 5, '2025-08-06 16:13:17', NULL, NULL, 22);
INSERT INTO `comment` VALUES (18, '应该是用来调色的吧', 5, '2025-08-06 16:13:34', NULL, NULL, 6);
INSERT INTO `comment` VALUES (19, '哇塞，看着就好吃', 5, '2025-08-06 16:13:43', NULL, NULL, 2);
INSERT INTO `comment` VALUES (20, '哈哈哈你加油', 5, '2025-08-06 16:13:55', NULL, 13, 10);
INSERT INTO `comment` VALUES (21, '我也想去演唱会！可惜没抢到许嵩的票', 5, '2025-08-06 16:14:33', NULL, NULL, 11);
INSERT INTO `comment` VALUES (22, '哈哈哈哈哈哈姐妹过于好笑了', 5, '2025-08-06 16:14:53', NULL, NULL, 14);
INSERT INTO `comment` VALUES (23, '是啊是啊，超级推荐的', 5, '2025-08-09 01:01:59', NULL, NULL, 22);
INSERT INTO `comment` VALUES (24, '很有感触的一场观影', 5, '2025-08-09 01:02:13', NULL, 23, 22);
INSERT INTO `comment` VALUES (25, '可以互相加个**沟通一下吗？', 1, '2025-08-09 01:02:27', 24, 23, 22);
INSERT INTO `comment` VALUES (26, '11111', 1, '2025-09-01 22:24:03', NULL, NULL, 34);
INSERT INTO `comment` VALUES (27, '太好了', 1, '2025-09-01 22:24:09', NULL, 26, 34);
INSERT INTO `comment` VALUES (28, '444444', 1, '2025-09-01 22:26:36', NULL, NULL, 35);
INSERT INTO `comment` VALUES (29, '555555', 2, '2025-09-02 09:39:27', NULL, NULL, 38);
INSERT INTO `comment` VALUES (30, '太好了', 1, '2025-09-02 09:39:42', NULL, 29, 38);
INSERT INTO `comment` VALUES (31, '3333', 2, '2025-09-02 09:41:22', NULL, NULL, 38);

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '关注id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '关注管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (19, 1, 2);
INSERT INTO `follow` VALUES (8, 1, 5);
INSERT INTO `follow` VALUES (18, 2, 1);
INSERT INTO `follow` VALUES (9, 5, 1);
INSERT INTO `follow` VALUES (2, 5, 4);

-- ----------------------------
-- Table structure for friend_request
-- ----------------------------
DROP TABLE IF EXISTS `friend_request`;
CREATE TABLE `friend_request`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `from_user_id` int(11) NULL DEFAULT NULL COMMENT '申请人id',
  `to_user_id` int(11) NULL DEFAULT NULL COMMENT '被申请人id',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态(0:待处理,1:已同意,2:已拒绝)',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '申请消息',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '申请时间',
  `handle_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `users`(`from_user_id`, `to_user_id`) USING BTREE,
  INDEX `to_user_id`(`to_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '好友申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of friend_request
-- ----------------------------
INSERT INTO `friend_request` VALUES (1, 1, 5, 1, '你好，我想和你成为好友！', '2025-09-02 07:05:49', '2025-09-02 07:06:53');
INSERT INTO `friend_request` VALUES (2, 2, 1, 1, '你好，我想和你成为好友！', '2025-09-02 09:38:40', '2025-09-02 09:38:54');

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '点赞用户id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '被点赞博文id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '点赞管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES (1, 2, 38, '2025-09-02 09:33:30');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '通知内容',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '通知类型(like/follow/comment/collect/mention/friend_request/friend_accept)',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发送时间',
  `from_user_id` int(11) NULL DEFAULT NULL COMMENT '来自用户id',
  `to_user_id` int(11) NULL DEFAULT NULL COMMENT '去往用户id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '关联id',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读',
  `read_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '阅读时间',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'social' COMMENT '消息分类(system/social/private)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (2, '收藏了你的笔记', '收藏', '2025-08-06 16:11:03', 5, 5, 24, 1, '2025-09-02 07:07:41', 'system');
INSERT INTO `message` VALUES (3, '收藏了你的笔记', '收藏', '2025-08-06 16:11:11', 1, 5, 24, 1, '2025-09-02 07:07:41', 'system');
INSERT INTO `message` VALUES (4, '收藏了你的笔记', '收藏', '2025-08-06 16:11:14', 1, 4, 19, 0, NULL, 'system');
INSERT INTO `message` VALUES (5, '收藏了你的笔记', '收藏', '2025-08-06 16:11:31', 1, 4, 19, 0, NULL, 'system');
INSERT INTO `message` VALUES (6, '收藏了你的笔记', '收藏', '2025-08-06 16:11:48', 3, 5, 23, 1, '2025-09-02 07:07:41', 'system');
INSERT INTO `message` VALUES (7, '收藏了你的笔记', '收藏', '2025-08-06 16:12:30', 4, 5, 24, 1, '2025-09-02 07:07:39', 'system');
INSERT INTO `message` VALUES (8, '收藏了你的笔记', '收藏', '2025-09-01 22:24:15', 1, 1, 34, 1, '2025-09-01 23:43:29', 'system');
INSERT INTO `message` VALUES (9, '请求添加你为好友', 'friend_request', '2025-09-02 07:05:50', 1, 5, NULL, 1, '2025-09-02 07:06:49', 'social');
INSERT INTO `message` VALUES (10, '同意了你的好友申请', 'friend_accept', '2025-09-02 07:06:53', 5, 1, NULL, 1, '2025-09-02 07:07:12', 'social');
INSERT INTO `message` VALUES (11, '关注了你', 'follow', '2025-09-02 09:31:18', 2, 1, NULL, 1, '2025-09-02 09:31:33', 'social');
INSERT INTO `message` VALUES (12, '点赞了你的笔记', 'like', '2025-09-02 09:33:30', 2, 1, 38, 1, '2025-09-02 09:33:44', 'social');
INSERT INTO `message` VALUES (13, '收藏了你的笔记', '收藏', '2025-09-02 09:33:31', 2, 1, 38, 1, '2025-09-02 09:33:43', 'social');
INSERT INTO `message` VALUES (14, '关注了你', 'follow', '2025-09-02 09:33:58', 1, 2, NULL, 1, '2025-09-02 09:34:09', 'social');
INSERT INTO `message` VALUES (15, '关注了你', 'follow', '2025-09-02 09:38:18', 2, 1, NULL, 1, '2025-09-02 09:38:23', 'social');
INSERT INTO `message` VALUES (16, '关注了你', 'follow', '2025-09-02 09:38:27', 1, 2, NULL, 1, '2025-09-02 09:38:37', 'social');
INSERT INTO `message` VALUES (17, '请求添加你为好友', 'friend_request', '2025-09-02 09:38:40', 2, 1, NULL, 1, '2025-09-02 09:38:46', 'social');
INSERT INTO `message` VALUES (18, '同意了你的好友申请', 'friend_accept', '2025-09-02 09:38:54', 1, 2, NULL, 1, '2025-09-02 09:39:00', 'social');

-- ----------------------------
-- Table structure for notification_setting
-- ----------------------------
DROP TABLE IF EXISTS `notification_setting`;
CREATE TABLE `notification_setting`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `like_notify` tinyint(1) NULL DEFAULT 1 COMMENT '点赞通知',
  `follow_notify` tinyint(1) NULL DEFAULT 1 COMMENT '关注通知',
  `comment_notify` tinyint(1) NULL DEFAULT 1 COMMENT '评论通知',
  `collect_notify` tinyint(1) NULL DEFAULT 1 COMMENT '收藏通知',
  `mention_notify` tinyint(1) NULL DEFAULT 1 COMMENT '提及通知',
  `private_notify` tinyint(1) NULL DEFAULT 1 COMMENT '私信通知',
  `friend_request_notify` tinyint(1) NULL DEFAULT 1 COMMENT '好友申请通知',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification_setting
-- ----------------------------
INSERT INTO `notification_setting` VALUES (1, 1, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `notification_setting` VALUES (2, 2, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `notification_setting` VALUES (3, 3, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `notification_setting` VALUES (4, 4, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `notification_setting` VALUES (5, 5, 1, 1, 1, 1, 1, 1, 1);

-- ----------------------------
-- Table structure for private_message
-- ----------------------------
DROP TABLE IF EXISTS `private_message`;
CREATE TABLE `private_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `from_user_id` int(11) NULL DEFAULT NULL COMMENT '发送者id',
  `to_user_id` int(11) NULL DEFAULT NULL COMMENT '接收者id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息内容',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读',
  `send_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `to_user_id`(`to_user_id`) USING BTREE,
  INDEX `conversation`(`from_user_id`, `to_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '私信消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of private_message
-- ----------------------------
INSERT INTO `private_message` VALUES (1, 1, 5, '你好', 1, '2025-09-02 07:07:29');
INSERT INTO `private_message` VALUES (2, 5, 1, '4444', 1, '2025-09-02 07:11:36');
INSERT INTO `private_message` VALUES (3, 1, 5, '很高兴认识你', 1, '2025-09-02 07:25:32');
INSERT INTO `private_message` VALUES (4, 1, 5, '太好了', 1, '2025-09-02 07:29:36');
INSERT INTO `private_message` VALUES (5, 5, 1, '不错', 1, '2025-09-02 07:29:49');

-- ----------------------------
-- Table structure for sys_admin
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_admin
-- ----------------------------
INSERT INTO `sys_admin` VALUES (1, 'admin', 'admin', '管理员', 'http://127.0.0.1:9090/web/download/e73c2e98e1f04b05b9279b7f73e538ef.webp', 'admin@email.com', '18888888888');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '111', '111', '王兰花', 'http://127.0.0.1:9090/web/download/aa2304c703284d8c9213e45ccbef642d.webp', 'user@email.com', '18888888888', '爱猫人士,最爱小猫啦');
INSERT INTO `sys_user` VALUES (2, '222', '222', '宇宙之大', 'http://127.0.0.1:9090/web/download/189b31b9e4804bf38a530e8ca20a5ee3.jpeg', 'user@email.com', '18888888888', '我在南极拍下了企鹅与冰川的剪影');
INSERT INTO `sys_user` VALUES (3, '333', '333', '明智之举', 'http://127.0.0.1:9090/web/download/b42d059f36884e87a7957c3f50b71bdc.webp', 'xxxxx@163.com', '178866998877', '想必你也看过了一些风景～');
INSERT INTO `sys_user` VALUES (4, '444', '444', '科幻', 'http://127.0.0.1:9090/web/download/357e91c4c5ce4a9a978987d2f32f8095.webp', 'kehuan@163.com', '1789990009988', '她的升空用了七秒半');
INSERT INTO `sys_user` VALUES (5, '555', '555', '愿望', 'http://127.0.0.1:9090/web/download/fe6b61620fe4440bb6a3adc529bbab8c.webp', 'ywywyw@qq.com', '179888667766', '愿望成真吧');

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '分类管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES (1, '教育', '教育');
INSERT INTO `type` VALUES (2, '穿搭', '穿搭');
INSERT INTO `type` VALUES (3, '美食', '美食');
INSERT INTO `type` VALUES (4, '彩妆', '彩妆');
INSERT INTO `type` VALUES (5, '影视', '影视');
INSERT INTO `type` VALUES (6, '游戏', '游戏');
INSERT INTO `type` VALUES (7, '职场', '职场');
INSERT INTO `type` VALUES (8, '情感', '情感');
INSERT INTO `type` VALUES (9, '家居', '家居');
INSERT INTO `type` VALUES (10, '萌宠', '萌宠');

-- ----------------------------
-- Table structure for word
-- ----------------------------
DROP TABLE IF EXISTS `word`;
CREATE TABLE `word`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '敏感词',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '敏感词管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of word
-- ----------------------------
INSERT INTO `word` VALUES (1, 'QQ', '广告');
INSERT INTO `word` VALUES (2, '微信', '广告');

-- ----------------------------
-- View structure for friendship
-- ----------------------------
DROP VIEW IF EXISTS `friendship`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `friendship` AS SELECT 
  f1.user_id as user1_id,
  f1.item_id as user2_id,
  LEAST(f1.user_id, f1.item_id) as friendship_id
FROM follow f1
JOIN follow f2 ON f1.user_id = f2.item_id AND f1.item_id = f2.user_id ;

-- ----------------------------
-- Records of word
-- ----------------------------
INSERT INTO `word` VALUES (1, 2, 1);
INSERT INTO `word` VALUES (2, 1, 1);
INSERT INTO `word` VALUES (5, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
