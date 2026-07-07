# Movie Filming Locations

A Spring Boot web app that maps verified filming locations of movies,
with an async LLM extraction pipeline (cross-validated against Wikidata
and OSM geocoding) and constrained TSP itinerary optimization.

## Architecture
Frontend (Leaflet map) → Spring Boot REST API → PostgreSQL
+ async extraction & validation pipeline. (架构草图待补)

## Status
MVP in progress — see PROJECT_PLAN.md