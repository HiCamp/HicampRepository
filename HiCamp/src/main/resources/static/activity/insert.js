const url = "http://localhost:8080/HiCamp/activity/inserttest";
const btnPreview = document.querySelector(".my-preview-data-btn")

const btnFilldata = document.querySelector(".my-filldata-data-btn");

// 一鍵輸入:

function fillData() {
  document.getElementById("fillData").addEventListener("click", function () {
    document.getElementById("activityType").value = "長天數縱走";
    document.getElementById("activityName").value =
      "一生必朝聖: 水鹿大軍圍繞的北三段(能高安東軍)縱走六天五夜";
    document.getElementById("activityLocation").value = "太魯閣國家公園";
    document.getElementById("activityInfo").value =
      "雖然說玉山、雪山是台灣最高的山峰，但是在許多台灣山岳人的心目中，「帝王之山」的封號，恐怕得頒給中央山脈北一段的「南湖大山」，而南湖大山甚至還不是中央山脈最高峰呢。而「南湖大山」能夠有這樣的稱號，得自於其威儀厚重、氣勢偉岸的山型，不管從任何角度、任何距離看過去，南湖大山皆是一派端凝莊嚴之氣象。除此之外，南湖山區並不只有南湖大山這一座山，還有數座高低不等、有著各自獨特性格的山峰，有如眾星拱月般護衛著南湖大山主峰，因此更具帝王之相的意義啦！而除了數座高聳入雲的百岳名峰之外，南湖山區有著台灣最完整多變的冰河地形（如壯麗的上、下圈谷）、台灣唯一的「寒漠」地質、台灣最長的冰封雪季、還有一些世界獨有的原生物種....甚至南湖山區似乎帶著某種鍾靈毓秀的天地靈氣，讓南湖大山成為許多愛山人魂牽夢縈的高山美地，總是一而再、再而三地多次拜訪，有些人認為必須春夏秋冬四季都走過一遍，才算是完整的登過南湖大山。";
    document.getElementById("activityQuota").value = "35";
    document.getElementById("activityPrice").value = "12000";
  });
}
document.getElementById("fillData").addEventListener("click", fillData);

























































// 用不到沒寫完

const btnSubmit = document.querySelector(".my-submit-data-btn");
const btnReset = document.querySelector(".my-reset-data-btn");

btnSubmit.addEventListener("click", (event) => {
  const activityType = document.getElementById("activityType").value;
  const activityName = document.getElementById("activityName").value;
  const activityLocation = document.getElementById("activityLocation").value;
  const activityInfo = document.getElementById("activityInfo").value;
  const activityQuota = document.getElementById("activityQuota").value;
  const activityPrice = document.getElementById("activityPrice").value;
  const activityFileName = document.getElementById("activityFileName").value;
  const activityPicture = document.getElementById("activityPicture");

  let dtoObject = {
    activityType: activityType,
    activityName: activityName,
    activityLocation: activityLocation,
    activityInfo: activityInfo,
    activityQuota: activityQuota,
    activityPrice: activityPrice,
    activityFileName: activityFileName,
    activityPicture: activityPicture
  };

  const dtoJsonString = JSON.stringify(dtoObject);

  $.ajax({
    method: "post",
    url: url,
    contentType: "application/json;charset=UTF-8",
    data: dtoJsonString,
    success: function (response) {
      activityType = document.getElementById("activityType").value = '';
      activityName = document.getElementById("activityName").value = '';
      activityLocation = document.getElementById("activityLocation").value = '';
      activityInfo = document.getElementById("activityInfo").value = '';
      activityQuota = document.getElementById("activityQuota").value = '';
      activityPrice = document.getElementById("activityPrice").value = '';
      activityFileName = document.getElementById("activityFileName").value = '';
      activityPicture = document.getElementById("activityPicture") = '';
      console.log(response);
    },
    error: function (err) {
      console.log('error');
    }


  });

});

function submitData() {
  var activityType = document.getElementById("activityType").value;
  var activityName = document.getElementById("activityName").value;
  var activityLocation = document.getElementById("activityLocation").value;
  var activityInfo = document.getElementById("activityInfo").value;
  var activityQuota = document.getElementById("activityQuota").value;
  var activityPrice = document.getElementById("activityPrice").value;
  var activityPicture = document.getElementById("activityPicture");
  var activityFileName = document.getElementById("activityFileName").value;

  var data = new FormData();
  data.append("activityType", activityType);
  data.append("activityName", activityName);
  data.append("activityLocation", activityLocation);
  data.append("activityInfo", activityInfo);
  data.append("activityQuota", activityQuota);
  data.append("activityPrice", activityPrice);
  data.append("activityPicture", activityPicture);
  data.append("activityFileName", activityFileName);

  axios
    .post("http://localhost:8080/HiCamp/activity/insert", data)
    .then(function (response) {
      console.log(response.data);
    })
    .catch(function (error) {
      console.error(error);
    });
}

function resetData() {





}
