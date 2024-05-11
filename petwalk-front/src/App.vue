<template>
  <a href="http://localhost:8080/oauth2/authorization/google">구글 로그인하기</a>
  <p>accesstoken : {{accessToken}}</p>
</template>

<script>

export default {
  name: 'App',
  data() {
    return {
      accessToken: null,
    };
  },
  mounted() {
    this.accessToken = this.$cookies.get('Authorization-refresh');
    console.log(this.accessToken);
  },
};
</script>

<style>

</style>
