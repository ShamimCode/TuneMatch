# TuneMatch

A music recommendation system that suggests songs based on user listening behavior and audio characteristics, using content-based similarity and collaborative filtering.

## Overview

TuneMatch started as a rule-based recommendation engine (JSP/Servlets/MySQL) and is being rebuilt as a modern full-stack application with a dedicated machine learning microservice for smarter recommendations.

## Tech Stack

| Layer           | Technology                            |
| --------------- | ------------------------------------- |
| Frontend        | React (Vite)                          |
| Backend API     | Spring Boot (Java)                    |
| ML Microservice | Python, FastAPI, scikit-learn, pandas |
| Database        | MySQL                                 |
| Dataset         | 3,000 curated songs across 12 genres  |

## Project Structure

```
tunematch/
├── frontend/       # React app - UI, song browsing, playlists
├── backend/        # Spring Boot REST API - users, songs, playlists, interactions
├── ml-service/     # FastAPI service - cosine similarity, collaborative filtering
├── data/           # Dataset, schema.sql, data generation scripts
└── README.md
```

## Core Features

- Browse and search songs across 12 genres
- Track user interactions (play, like, skip, dislike) to personalize recommendations
- Content-based recommendations using audio features (tempo, energy, valence, danceability)
- Collaborative filtering based on similar users' listening patterns
- Create and manage playlists

## Getting Started

### Prerequisites

- Java 17+
- Node.js
- Python 3.10+
- MySQL 8.0+

### Database Setup

```bash
mysql -u root -p < data/schema.sql
```

### Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

### ML Service (FastAPI)

```bash
cd ml-service
source venv/bin/activate
uvicorn main:app --reload --port 8000
```

### Frontend (React)

```bash
cd frontend
npm install
npm run dev
```

## Architecture

The Spring Boot backend handles core CRUD operations (users, songs, playlists) and persists user interactions to MySQL. The FastAPI ML microservice reads interaction and song data to generate recommendations via cosine similarity (content-based) and collaborative filtering, exposing results back to the backend through internal API calls.

## Status

🚧 Actively in development
