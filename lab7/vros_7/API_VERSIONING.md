# Strategia de versionare și migrare a API-ului VROS

## 1. Metoda de versionare
Folosim versionare prin URI: `/api/v1/...`, `/api/v2/...`.
Versiunea apare explicit în URL, deci e ușor de testat și de documentat.

## 2. Versiuni existente

| Versiune | Endpoint-uri                                              | Status                          |
|----------|-----------------------------------------------------------|---------------------------------|
| v1       | /api/v1/customers, /vehicles, /deliveries, /scenarios     | activă; **customers: deprecated** |
| v2       | /api/v2/customers                                         | activă, recomandată             |

## 3. Ce s-a schimbat în v2 (clienți)
- Răspunsurile includ link-uri HATEOAS în format HAL (`_links`):
  `self`, `deliveries`, `customers`.
- Endpoint nou: `GET /api/v2/customers/{id}/deliveries` (livrările unui client).
- Câmpurile clientului (id, name, email, phone, address, latitude, longitude) sunt aceleași ca în v1.
- Motivul unei versiuni noi: forma JSON se schimbă (apare `_links`), deci e o schimbare
  incompatibilă (breaking change) pentru clienții existenți.

## 4. Reguli
- Schimbările **compatibile** (câmp nou opțional, endpoint nou) se fac în versiunea existentă.
- Schimbările **incompatibile** (câmp redenumit/șters, structură diferită) cer o versiune nouă.
- O versiune publicată nu se modifică incompatibil.

## 5. Etapele migrării v1 → v2 (clienți)
1. **Lansare** – v2 este publicată; v1 și v2 funcționează în paralel.
2. **Depreciere** – v1 este marcată deprecated:
   - în Swagger UI (`@Deprecated` pe `CustomerController`);
   - în fiecare răspuns v1, prin headerele HTTP:
     `Deprecation: true`, `Sunset: Wed, 30 Jun 2027 23:59:59 GMT`,
     `Link: </api/v2/customers>; rel="successor-version"`.
3. **Tranziție** (până la 30 iunie 2027) – clienții trec pe v2; se monitorizează utilizarea v1.
4. **Retragere** – după data Sunset, v1 răspunde cu `410 Gone` și un link către v2,
   apoi codul v1 este eliminat.

## 6. Ghid pentru clienții API-ului
1. Înlocuiți `/api/v1/customers` cu `/api/v2/customers`.
2. Datele clientului se citesc la fel (aceleași câmpuri).
3. Folosiți link-urile din `_links` în loc să construiți URL-urile manual
   (ex.: livrările unui client se obțin din `_links.deliveries.href`).
4. Vehiculele, livrările și scenariile rămân deocamdată pe v1 (nu au schimbări incompatibile).


// curl.exe -i http://localhost:8081/api/v1/customers/1