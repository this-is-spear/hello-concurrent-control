import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    stages: [
        {duration: '10m', target: 200}, // VU로 램프업
        {duration: '5m', target: 200}, // VU를 유지
        {duration: '5m', target: 0}, // 0개의 VU로 램프다운
    ],
    thresholds: {
        'http_req_duration': ['p(95)<1500'], // 95번째 백분위 응답시간이 1500ms 미만이어야 함
        'http_req_failed': ['rate<0.01'], // 실패율이 1% 미만이어야 함
    },
};

const apiAddress = 'http://localhost:8080';
const issuers = ['1234', '2345'];

function isValidNumber(numStr) {
    const uniqueDigits = new Set(numStr.split('')).size;
    if (uniqueDigits !== 4) {
        return false;
    }

    if (issuers.includes(numStr)) {
        return false;
    }

    const digits = numStr.split('').map(Number);
    let isSequential = true;
    for (let i = 0; i < digits.length - 1; i++) {
        if (digits[i + 1] !== digits[i] + 1) {
            isSequential = false;
            break;
        }
    }

    return !isSequential;
}

export default function () {
    const sourceAccount = 3456;
    let targetAccount = 7890;

    const header = {
        headers: {
            'Content-Type': 'application/json'
        },
    }

    const transaction1Payload = {
        sourceAccount: "1234",
        targetAccount: sourceAccount,
        amount: 100,
    };
    const transaction1 = http.post(`${apiAddress}/transaction`, JSON.stringify(transaction1Payload), header);
    check(transaction1, {
        'transaction1 points successful': (resp) => {
            if (resp.status === 200) {
                return true;
            }
            console.log("transaction1" + resp.body);
        },
    });

    const transaction2Payload = {
        sourceAccount: sourceAccount,
        targetAccount: targetAccount,
        amount: 100,
    };

    const transaction2 = http.post(`${apiAddress}/transaction`, JSON.stringify(transaction2Payload), header);
    check(transaction2, {
        'transaction2 points successful': (resp) => {
            if (resp.status === 200) {
                return true;
            }
            console.log("transaction2" + resp.body);
        },
    });

    const transaction3Payload = {
        sourceAccount: targetAccount,
        targetAccount: "1234",
        amount: 100,
    };

    const transaction3 = http.post(`${apiAddress}/transaction`, JSON.stringify(transaction3Payload), header);
    check(transaction3, {
        'transaction3 points successful': (resp) => {
            if (resp.status === 200) {
                return true;
            }
            console.log("transaction3" + resp.body);
        },
    });

    sleep(1);
}
