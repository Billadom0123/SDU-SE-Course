const data = '[{ "name": "Flavio", "age": 35 }]'
const data3 = '[{ "name": "a", "start": 0, "end": 4 },{ "name": "b", "start": 0, "end": 4 },{ "name": "c", "start": 1, "end": 2 },{ "name": "d", "start": 2, "end": 3 }]'
try {
  const user = JSON.parse(data)
  console.log(user);
  console.log(JSON.parse(data3));
} catch(err) {
  console.error(err)
}