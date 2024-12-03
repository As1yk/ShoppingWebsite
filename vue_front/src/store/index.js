// src/store/index.js
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

// Vuex Store
export default new Vuex.Store({
    state: {
      username: null, // 初始化为空
    },
    mutations: {
      setUsername(state, username) {
        state.username = username;
      },
    },
    actions: {
      setUsername({ commit }, username) {
        commit('setUsername', username);
      },
    },
    getters: {
      username: (state) => state.username,
    },
  });
  
