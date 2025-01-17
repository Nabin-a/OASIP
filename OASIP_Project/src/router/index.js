import { createRouter, createWebHashHistory } from "vue-router";
import EventDetail from "../components/EventDetail.vue";
import EventList from "../components/EventList.vue";
import Home from "../views/Home.vue";
import User from "../views/User.vue"
import Event from "../views/Event.vue"
import Login from "../views/Login.vue"
import Register from "../views/Register.vue"
import Accessdenied from "../views/Accessdenied.vue"
import EventBook from "../views/EventBook.vue"
import Match from "../views/Match.vue"

const history = createWebHashHistory(import.meta.env.BASE_URL);
const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/users",
    name: "User",
    component: User,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/event",
    name: "Event",
    component: Event,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/access-denied",
    name: "Access denied",
    component: Accessdenied
  },
  {
    path: "/reserve",
    name: "Reserve",
    component: EventBook, 
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/match",
    name: "Match",
    component: Match,
    meta: {
      requiresAuth: true
    }
  }
//   { path: "/event/:id", name: "Detail", component: EventDetail },
];

const router = createRouter({ history, routes });

router.beforeEach((to,from,next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!localStorage.getItem('token')) {
      if(to.path==='/' || to.path==='/reserve') {
        next()
      } else
       next({ name: 'Login' })
    } else if (localStorage.getItem("role")=='ROLE_student'){
      if (to.path==='/users' || to.path==='/match' || to.path==='/regsiter') {
        next('/access-denied')
      } else {
        next()
      }
    } else if (localStorage.getItem("role")=='ROLE_admin') {
      if (to.path==='/users' || to.path==='/event' || to.path==='/match') {
        next()      
    } else {
        next()
    }
  }  else if (localStorage.getItem("role")=='ROLE_lecturer') {
      if (to.path==='/users' || to.path==='/reserve' || to.path==='/match' || to.path==='/register') {
        next('/access-denied')
      } else {
        next()
      }
  }
} else  {
    next()
  }
})

export default router;
