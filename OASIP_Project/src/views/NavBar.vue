<script setup>
import { useRouter } from "vue-router";

const appRouter = useRouter();
const goHome = () => appRouter.push({ name: "Home" });
const goUser = () => appRouter.push({ name: "User" });
const goEvent = () => appRouter.push({ name: "Event" });
const goLogin = () => appRouter.push({ name: "Login" });
const goBooking = () => appRouter.push({ name: "Reserve"})

let token = localStorage.getItem("token");
let name = localStorage.getItem("email");
let role = localStorage.getItem("role");

const clearData = () => localStorage.clear();
const reload = () => location.reload();

document.addEventListener("DOMContentLoaded", function(){
  window.addEventListener('scroll', function() {
      if (window.scrollY > 50) {
        document.getElementById('navbar_top').classList.add('fixed-top');
        // add padding top to show content behind navbar
        
        
      } else {
        document.getElementById('navbar_top').classList.remove('fixed-top');
         // remove padding top from body
        document.body.style.paddingTop = '0';
      } 
  });
});

</script>

<template>
  <nav id="navbar_top" class="navbar navbar-expand-lg navbar-light bg-nav shadow p-2 mb-5 nav">
    <div class="container-fluid">
      <button @click="goHome" class="OR"><img src="..\assets\oasip.png" width="30" height="30" class="logo"></button>
      <h1 class="bigtext">OASIP</h1>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">

        </ul>
        <li class="nav-item">
          <button @click="goHome" class="bd m-3">Home</button>
        </li>
       
        <li class="nav-item">
          <button 
          v-if="role != 'ROLE_lecturer'"
          @click="goBooking" class="bd m-3">Reserve</button>
        </li>

        <li class="nav-item">
          <button
            v-if="role == 'ROLE_admin'"
            @click="goUser"
            class="bd m-3"
          >
            User
          </button>
        </li>
        
        <li class="nav-item m-3">
          <button @click="goEvent" class="bd">Event</button>
        </li>
        
        <li class="nav-item text-light">
          <h1>|</h1>
        </li>
        &nbsp;
        <li v-if="token !== null" class="list-group item text-light m-2">
          {{ name }} 
        </li>

        &nbsp;
        <button
          v-if="token === null"
          class="bd"
          @click="goLogin"
        >
          Sign in
        </button>
        <button
          v-else="token !== null"
          class="btn btn-danger rounded-pill"
          @click="
            clearData();
            reload();
          "
        >
          Logout
        </button>
      </div>
    </div>
  </nav>
</template>

<style>
.OR {
  font-size: 34px;
  background: linear-gradient(to right, rgb(235, 61, 61), rgb(210, 144, 144));
  border: none;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.bd {
  padding: 0;
  border: none;
  background: none;
  color: white;
}

.logo {
  border-radius: 50%;
  
}

.bg-nav {
  background-color: #759bcafc;
}

.container-fluid {
  font-family:'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
}

.bigtext {
  font-size:45px;
  font-weight: bolder;
  margin-top:10px;
  margin-left: 20px;
  color:white
}

.nav-bar {}
</style>
