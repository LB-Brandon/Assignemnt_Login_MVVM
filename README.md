# 발생 이슈 목록
1. ViewModel 에서 String resource 를 가져올 수 없음
   1. https://www.youtube.com/watch?v=mB1Lej0aDus
   2. https://medium.com/@margin555/using-string-resources-in-a-viewmodel-e334611b73da
2. EmailProvider 검증 후 Email 검증을 하게 어떻게 만들지?
   1. ViewModel 에서 로직 구현하려고 하였으나 실패
   2. Activity 에서 Email 에러 liveData 의 데이터 존재 여부를 확인하여 Provider 후 Email 에러 띄우게 구현
   3. 