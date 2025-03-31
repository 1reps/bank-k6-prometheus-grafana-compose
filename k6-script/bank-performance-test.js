import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 10,  // 가상 사용자 수
  duration: '30s',
};

export default function() {
  // 계좌 잔액 조회 테스트
  let balanceResponse = http.get('http://app:8080/api/accounts/1/balance');
  check(balanceResponse, {
    'balance status is 200': (r) => r.status === 200,
  });

  sleep(1);

  // 계좌 이체 테스트
  let payload = JSON.stringify({
    sourceAccountId: 1,
    targetAccountId: 2,
    amount: 100.00,
    description: 'Test payment'
  });

  let params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let transferResponse = http.post('http://app:8080/api/payments/transfer', payload, params);
  check(transferResponse, {
    'transfer status is 200': (r) => r.status === 200,
  });

  sleep(2);
}