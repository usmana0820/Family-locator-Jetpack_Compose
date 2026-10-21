# Family Locator & Safety System

A family safety and location-sharing application built with **Kotlin, Jetpack Compose, and Django REST Framework**. The project aims to help family members stay connected through private groups, location sharing, and safety-focused features.

## Overview

Family Locator combines an Android application with a Django backend to support family group management and location-based safety services. The system is designed around secure access, location synchronization, and convenient communication between family members.

## Features

### Authentication & Accounts
- Google Sign-In integration
- JWT-based authentication
- Secure user sessions

### Family Group Management
- Create and join family groups
- Manage group membership
- Support for private family communities

### Location Tracking
- Share family members' locations
- Integrate location data with Google Maps
- Support background location updates
- Synchronize location information with the backend

### Safety & Emergency Features
- Safety zones using geofencing
- Entry and exit alerts for designated zones
- Emergency SOS with location sharing
- Push notifications for safety-related events

> **Note:** Confirm that each feature is implemented and tested in the current codebase before presenting it as production-ready. Features that are planned or partially implemented should be labeled accordingly.

## Technology Stack

| Component | Technologies |
|---|---|
| Android | Kotlin, Jetpack Compose |
| Architecture | MVVM |
| Backend | Python, Django, Django REST Framework |
| Database | PostgreSQL |
| Authentication | Google Sign-In, JWT |
| Maps & Location | Google Maps SDK, Fused Location Provider |
| Notifications | Firebase Cloud Messaging |

## Project Structure

The repository is organized into the following main areas:

- **`Jetpack Frontend/`** — Android application and user interface.
- **`Django Backend/Tracking App/`** — Backend and location-tracking services.

The exact internal structure may vary as the project evolves.

## Getting Started

### Prerequisites

- Android Studio
- Kotlin and Android SDK
- Python 3
- Django and Django REST Framework
- PostgreSQL, if configured as the backend database
- Google Maps and Firebase configuration for the relevant integrations

### 1. Clone the Repository

Because this repository is private, you must have access to it to clone the source code.

```bash
git clone https://github.com/usmana0820/Family-locator-Jetpack_Compose.git
cd Family-locator-Jetpack_Compose
```

### 2. Set Up the Django Backend

Navigate to the backend directory and create a virtual environment.

```bash
python -m venv venv
```

Activate it on Windows:

```bash
venv\Scripts\activate
```

Install the dependencies using the project's requirements file, if available:

```bash
pip install -r requirements.txt
```

Apply database migrations:

```bash
python manage.py migrate
```

Start the development server:

```bash
python manage.py runserver
```

**Important:** Run these commands from the directory containing `manage.py`. Configure the database, environment variables, and dependencies according to the project's actual backend settings.

### 3. Set Up the Android Application

1. Open the `Jetpack Frontend` project in Android Studio.
2. Allow Gradle to synchronize dependencies.
3. Configure the required Google Maps and Firebase credentials.
4. Set the backend API URL for your development environment.
5. Run the application on an Android device or emulator.
6. Grant the location and notification permissions required by enabled features.

## System Workflow

1. A user signs in to the application.
2. The user creates a family group or joins an existing one.
3. Authorized members share location information.
4. The Android application displays available location data.
5. Configured safety zones can trigger entry or exit alerts.
6. Emergency functionality can share location information with family members when implemented and configured.

## Security & Privacy

Location data is sensitive. A production deployment should include:

- Authentication and authorization checks on protected API endpoints
- HTTPS for communication between the Android application and backend
- Appropriate location permissions and clear user consent
- Secure storage of authentication tokens
- Restricted access to family groups and their location data
- Careful handling of location history, emergency events, and notification payloads

## Future Enhancements

Potential improvements include:

- Location history and timeline
- Route playback
- Driving and speed alerts
- Battery status sharing
- Crash detection
- Multi-device support

## Author

**Usmana Zulfiqar**

Full-Stack Software Engineer

*Kotlin · Jetpack Compose · Flutter · Django REST Framework · PostgreSQL*

- GitHub: [@usmana0820](https://github.com/usmana0820)
- Email: usmanazulfiqar20@gmail.com

---

*Family Locator & Safety System — helping families stay connected and informed.*
