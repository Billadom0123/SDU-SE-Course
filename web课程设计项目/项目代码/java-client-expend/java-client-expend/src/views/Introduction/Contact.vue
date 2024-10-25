<template>
  <IntroNavi/>
  <div style="padding: 50px 0 30px;;text-align: center;">
    <h2 >联系我</h2>
  </div>

  <section id="contact" style="background: #f9fbfd;
    padding: 50px 0 100px;">
    <div class="section-heading text-center">
      <h2>contact me（仅前端界面）</h2>
    </div>
    <div class="container" style="margin-left:auto;margin-right:auto">
      <div class="contact-content">
        <div class="row">
          <div class="col-md-offset-1 col-md-5 col-sm-6">
            <div class="single-contact-box">
              <div class="contact-form">
                <form>
                  <div class="row">
                    <div class="col-sm-6 col-xs-12">
                      <div class="form-group">
                        <input type="text" class="form-control" id="name" placeholder="Name*" name="name">
                      </div><!--/.form-group-->
                    </div><!--/.col-->
                    <div class="col-sm-6 col-xs-12">
                      <div class="form-group">
                        <input type="email" class="form-control" id="email" placeholder="Email*" name="email">
                      </div><!--/.form-group-->
                    </div><!--/.col-->
                  </div><!--/.row-->
                  <div class="row">
                    <div class="col-sm-12">
                      <div class="form-group">
                        <input type="text" class="form-control" id="subject" placeholder="Subject" name="subject">
                      </div><!--/.form-group-->
                    </div><!--/.col-->
                  </div><!--/.row-->
                  <div class="row">
                    <div class="col-sm-12">
                      <div class="form-group">
                        <textarea class="form-control" rows="8" id="comment" placeholder="Message" ></textarea>
                      </div><!--/.form-group-->
                    </div><!--/.col-->
                  </div><!--/.row-->
                  <div class="row">
                    <div class="col-sm-12">
                      <div class="single-contact-btn">
                        <a class="contact-btn" href="#" role="button">submit</a>
                      </div><!--/.single-single-contact-btn-->
                    </div><!--/.col-->
                  </div><!--/.row-->
                </form><!--/form-->
              </div><!--/.contact-form-->
            </div><!--/.single-contact-box-->
          </div><!--/.col-->
          <div class="col-md-offset-1 col-md-5 col-sm-6">
            <div class="single-contact-box">
              <div class="contact-adress">
                <div class="contact-add-head">
                  <h3>姓名</h3>
                  <p>马亿祥</p>
                </div>
                <div class="contact-add-info">
                  <div class="single-contact-add-info">
                    <h3>电话</h3>
                    <p>18721889925</p>
                  </div>
                  <div class="single-contact-add-info">
                    <h3>email</h3>
                    <p>mayixiang2@126.com</p>
                  </div>
                  <div class="single-contact-add-info">
                    <h3>blog</h3>
                    <p>114.116.19.125</p>
                  </div>
                </div>
              </div><!--/.contact-adress-->

            </div><!--/.single-contact-box-->
          </div><!--/.col-->

        </div><!--/.row-->
      </div><!--/.contact-content-->
    </div><!--/.container-->

  </section>
</template>

<script>
import IntroNavi from "../../components/IntroNavi.vue";
import 'jquery/dist/jquery.min'

import 'bootstrap/dist/css/bootstrap.css'

export default {
  name: "Contact",
  components: {IntroNavi}
}
</script>

<style scoped>
.contact {
  background: #f9fbfd;
  padding: 50px 0 100px;
}
.single-contact-box {padding-top: 100px;}
.contact-form  .form-group {
  margin-bottom: 20px;

}
.contact-form .form-control {
  padding: 25px;
  font-size: 14px;
  border: 1px solid #fff;
  border-radius: 0px;
  box-shadow: none;
  outline: 0!important;
  background: #fff;
  color: #636a82;
  resize: none;
}
.contact-form .single-contact-btn {
  display:flex;
  align-items:center;
  justify-content:center;
  height:50px;
  text-transform:capitalize;
  margin-top: 3px;
  background: #b636ff;
  border: 1px solid #b636ff;
  cursor:pointer;
  box-shadow: 0 5px 20px rgba(0,0,0,.2);
  -webkit-transition: .5s;
  -moz-transition:.5s;
  -ms-transition:.5s;
  -o-transition:.5s;
  transition: .5s;
}
.contact-form .single-contact-btn .contact-btn ,
.contact-form .single-contact-btn .contact-btn:focus {
  margin-top:0px;
  outline: 0 !important;
  border:0;
  color: #fff;
  border-radius: 0px;
  font-size:14px;
  -webkit-transition: .5s;
  -moz-transition:.5s;
  -ms-transition:.5s;
  -o-transition:.5s;
  transition: .5s;

}
.single-contact-btn:hover{
  background: #9f00ff;
  border: 1px solid #9f00ff;
  box-shadow: 0 5px 20px rgba(0,0,0,.2);
}
.contact-add-head h3 {
  font-size:  24px;
  color:  #3c4258;
}
.contact-add-head p {
  font-size:  16px;
  text-transform: capitalize;
  color:  #636a82;
  font-weight:  300;
  margin-top:  6px;
}

.contact-add-info { margin: 42px 0 52px;}
.single-contact-add-info h3 {
  color:  #636a82;
  font-weight:  500;
}
.single-contact-add-info p {
  color:  #999fb3;
  font-size:  16px;
  margin-top:  18px;
}
.single-contact-add-info {
  margin-bottom:  26px;
}
</style>