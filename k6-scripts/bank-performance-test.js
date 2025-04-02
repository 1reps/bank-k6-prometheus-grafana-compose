import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  vus: 100,  // 가상 사용자 수
  duration: '30s',
};

export default function() {
  // 계좌 이체 테스트 - 직접 금액만 전송
  let payload = JSON.stringify(10000.00);

  let params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let transferResponse = http.post('http://host.docker.internal:8080/api/payments/accounts/1/deposit', payload, params);
  check(transferResponse, {
    'transfer status is 200': (r) => r.status === 200,
  });

  sleep(2);
}