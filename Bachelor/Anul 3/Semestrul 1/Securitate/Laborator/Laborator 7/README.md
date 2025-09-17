# Laborator 7 (14.11.2022)

### Notite Tabla Laborator:

### Exercitiul 1
<ol type="a">
  <li>A aparut o imagine normala (nimic suspicios).</li>
  <li>Imaginea ascunde un fisier executabil.</li>
  <li>Obtine un scor de 0/61.</li>
  <li>Obtine un scor de 10/72.</li>
  <li>Sunt necesare pentru a putea executa programul.</li>
  <li>Da, putem considera imaginea un malware, deoarece are ca payload ascuns un executabil cu intentii malitioase.</li>
</ol>

![WhatsApp Image 2022-11-14 at 17 36 12](https://user-images.githubusercontent.com/73616883/203178143-db97c3c0-42b2-412e-9e58-3c4846895c57.jpg)
![WhatsApp Image 2022-11-14 at 17 36 20](https://user-images.githubusercontent.com/73616883/203178165-bbd68a4b-a202-4e3c-91f2-192e19a44276.jpg)


### Exercitiul 2

![WhatsApp Image 2022-11-14 at 17 36 42](https://user-images.githubusercontent.com/73616883/203177847-ee54f717-9541-4ea5-b6ba-9d062b7b699b.jpg)

### Exercitiul 3
```python
import json
import hashlib
import requests


f1 = open("data.in", "rb")
f2 = open("data.json", "w", encoding="utf-8")


def sha256_file():
    sha256_content = hashlib.sha256()

    for block in iter(lambda: f1.read(4096), b""):
        sha256_content.update(block)

    return sha256_content.hexdigest()


def virustotal_api(sha256_file_key):

    url = 'https://www.virustotal.com/vtapi/v2/file/report'
    params = {'apikey': '419fe66ccb370af66d9425046fd34941d888ceb41a5f678f0504cedc50c15178',
              'resource': sha256_file_key}
    response = requests.get(url, params=params)
    json.dump(response.json(), f2, indent=2)


sha256_file_key = sha256_file()
virustotal_api(sha256_file_key)

f1.close()
f2.close()
```

Mi-am creat un cont pe [VirusTotal](https://www.virustotal.com/gui/home/upload), de unde am luat api key ```419fe66ccb370af66d9425046fd34941d888ceb41a5f678f0504cedc50c15178```.

Input:
```
Imi place mult securitatea hahahahahaha
```

Output:
```
{
  "response_code": 0,
  "resource": "e94229cbbe28f3d07479cfdfd952230f4f6c635d25d5eee6cbd2e16e418bd0ba",
  "verbose_msg": "The requested resource is not among the finished, queued or pending scans"
}
```

### Exercitiul 4
![WhatsApp Image 2022-11-14 at 17 36 33](https://user-images.githubusercontent.com/73616883/203177766-e0671b9a-739f-40a4-82e0-83a8bbd3e18f.jpg)

Vedem ca atunci cand citim parola nu verificam sa nu apara un buffer overflow. Din cum este pozitionata stiva, putem sa supra-scriem pass. Asadar, daca executam programul si punem ca input "aaaaaaaaaaaaaa" o sa suprascrie si variabila "pass" si va acepta inputul.

Acest tip de atac se numeste stack buffer overflow attack.
