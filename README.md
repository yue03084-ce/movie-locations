# Movie Filming Locations

Maps verified filming locations of movies with an LLM extraction pipeline
and route optimization.

> Target architecture below; currently building the MVP (map + REST API).
> Full pipeline: async LLM extraction cross-validated against Wikidata and
> OSM geocoding, plus constrained TSP itinerary optimization.

## Architecture
Frontend (Leaflet map) → Spring Boot REST API → PostgreSQL
+ async extraction & validation pipeline. 

## Status
MVP in progress — see PROJECT_PLAN.md