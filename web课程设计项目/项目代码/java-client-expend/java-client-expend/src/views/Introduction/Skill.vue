<template>
<IntroNavi/>

  <section id="skills" class="skills">
    <div style="padding:50px 0 80px;">
      <div class="section-heading text-center">
        <h2>skills</h2>
      </div>

  <div class="container">
    <div class="row">
      <div class="col-md-6">
        <div class="single-skill-content">
          <div class="barWrapper">
            <span class="progressText">adobe photoshop</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="30" aria-valuemin="70" aria-valuemax="100" style="width: 30%">

                </div>
              </div>
              <h3>30%</h3>
            </div>
          </div><!-- /.barWrapper -->
          <div class="barWrapper">
            <span class="progressText">vue</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="85" aria-valuemin="10" aria-valuemax="100" style="width: 75%">

                </div>
              </div>
              <h3>85%</h3>
            </div>
          </div><!-- /.barWrapper -->
          <div class="barWrapper">
            <span class="progressText">springboot</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="55" aria-valuemin="45" aria-valuemax="100" style="width: 55%">

                </div>
              </div>
              <h3>55%</h3>
            </div>
          </div><!-- /.barWrapper -->
          <div class="barWrapper">
            <span class="progressText">睡觉</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%;background:red">

                </div>
              </div>
              <h3>100%</h3>
            </div>
          </div><!-- /.barWrapper -->
        </div>
      </div>
      <div class="col-md-6">
        <div class="single-skill-content">
          <div class="barWrapper">
            <span class="progressText">html +css</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="90" aria-valuemin="10" aria-valuemax="100" style="width:90%">

                </div>
              </div>
              <h3>90%</h3>
            </div>
          </div><!-- /.barWrapper -->
          <div class="barWrapper">
            <span class="progressText">excel</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="65" aria-valuemin="35" aria-valuemax="100" style="width:65%;">

                </div>
              </div>
              <h3>65%</h3>
            </div>
          </div><!-- /.barWrapper -->
          <div class="barWrapper">
            <span class="progressText">java</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="50" aria-valuemin="50" aria-valuemax="100" style="width:50%;">

                </div>
              </div>
              <h3>50%</h3>
            </div>
          </div><!-- /.barWrapper -->
          <div class="barWrapper">
            <span class="progressText"> 吃饭</span>
            <div class="single-progress-txt">
              <div class="progress ">
                <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%;background: red">

                </div>
              </div>
              <h3>100%</h3>
            </div>
          </div><!-- /.barWrapper -->
        </div>
      </div>
    </div><!-- /.row -->
  </div>

    </div>
      </section>
</template>

<script setup>


// import {ref} from "vue";
import IntroNavi from "../../components/IntroNavi.vue";
import 'jquery/dist/jquery.min'
import 'bootstrap/dist/css/bootstrap.css'


</script>

<style>

.single-progress-txt {
  display: flex;
}
.single-progress-txt h3 {
  width: 10%;
  color: #a606c3;
  position: relative;
  top: 6px;
  left: 20px;
}
.barWrapper {
  font-weight: 400;
  font-size: 16px;
  text-transform: uppercase;
}
.progress {
  border-radius:3px;
  overflow: visible;
  background: #e6ecf3;
  height:8px;
  margin-top: 15px;
  width: 85%;
  margin-bottom: 37px;
}
.progress-bar {
  background: #b636ff;
  border-radius: 3px;
  -webkit-transition: width 1.5s ease-in-out;
  -ms-transition: width 1.5s ease-in-out;
  -moz-transition: width 1.5s ease-in-out;
  -o-transition: width 1.5s ease-in-out;
  transition: width 1.5s ease-in-out;

}
</style>