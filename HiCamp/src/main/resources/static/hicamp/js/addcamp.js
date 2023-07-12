const uploadBtn = document.querySelector('.my-campsite-data-btn');
const submitBtn = document.querySelector('.submitBtn');

uploadBtn.addEventListener('click', (e) => {
  e.preventDefault();
  console.log("click");

  let form = document.getElementById('myForm');
  let formdataObject = new FormData(form);
  sendPostRequest(formdataObject);
})
submitBtn.addEventListener('click', (e) => {
  e.preventDefault();
  window.location.href = 'http://localhost:8080/HiCamp/campsite/show';
});


function sendPostRequest(formData) {
  axios({
    method: 'post',
    url: 'http://localhost:8080/HiCamp/campsite/new',
    data: formData
    // headers:{"Content-Type":"multipart/form-data"} // axios 會自動做這件事，但是 fetch, XMLHttpRequest 不會自動做，要記得加
  })
.then(response => {
      let resultBlock = document.getElementById('upload_result');
      if (response.data && response.data.campsiteNo) {  // 檢查返回的數據是否包含campsiteNo欄位
        resultBlock.innerHTML = '新增成功';
        resultBlock.style.color = 'green';
      } else {
        resultBlock.innerHTML = '新增失敗，請重新再試';
        resultBlock.style.color = 'red';
      }
    })
    .catch(err => {
      console.log('err: ' + err)
      let resultBlock = document.getElementById('upload_result');
      resultBlock.innerHTML = '上傳失敗，請重新再試'
      resultBlock.style.color = 'red';
    })
    .finally(function () {
      document.getElementById('myForm').reset();
    })
}
