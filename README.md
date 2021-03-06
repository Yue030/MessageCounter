# Message Counter v2.0-SNAPSHOT

### 作者：月Yue

### 版本：2.0-SNAPSHOT

### 發布日期：2020/10/19

# 在使用之前

請先依照以下指示，獲取訊息檔

登入您的Facebook -> 設定 -> 你的Facebook資訊 -> 下載資訊

接下來請按下 全部取消勾選

找到訊息，並打勾

日期範圍看你想要獲取哪一個時間內的訊息

格式請使用JSON

媒體畫質看個人

最後按下建立檔案

接下來耐心等待檔案處理完畢

處理完之後

從Facebook下載下來

並找到個人需要查看的訊息

inbox -> 每個聯絡人的訊息

記住他的位置

#使用說明：

請先下載Java

否則將無法執行

首先會看到設定檔（config.properties）跟主程式

> (若設定檔打不開，可去下載notepad++, https://notepad-plus-plus.org/downloads/

設定檔內的東西皆可自己調整

```
Lang 語言（目前開放中英）

Order 排序方式（目前開放正序及倒敘，以訊息時間為準）

MessageFilePath 訊息檔路徑（可設置自動生成的訊息檔放置路徑）

Mode 模式（設置模式為IG或FB)
```

確認沒有要改之後

打開主程式

最上面的區塊是系統日誌，會有一些訊息在上面跑

左下角的區塊則是已選擇的檔案

使用時

先選擇檔案
> FB模式可選多個
>
> IG則只能有一個

確認無誤後，點擊計算訊息

將開始啟動程序

可能需要一點時間

結束後會跑出小視窗告知

並會在config設置的路徑創建一個檔案

裡面會是已經解碼並格式後的訊息

# 使用時，請您放心安全性的問題

由於只是純粹對檔案進行抓資料、解碼的動作

所以可以在離線狀況下使用

不會在您不注意的時候

將您的個人資訊上傳到資料庫

如還有對本程式有任何安全性疑慮

我們的代碼是Open Source的

您可去github查看我們的原始碼

Source Code:
https://github.com/Yue030/MessageCounter

下載連結:
https://1drv.ms/u/s!AhVvkALrnG3kbnhgaeJzkpb91Fk?e=3bfLZr

# 若對程式有任何問題，可以聯絡以下的Email

Email: zhikuan0322@outlook.com

看到的時候將會盡快的回覆您

謝謝您使用本程式
