from pathlib import Path
import os
import environ
from datetime import timedelta

# Initialize environment variables
env = environ.Env()
environ.Env.read_env()  # Read the .env file

# Base directory for the project
BASE_DIR = Path(__file__).resolve().parent.parent

# Read environment variables from .env file
environ.Env.read_env(BASE_DIR / ".env")

# Get FRONTEND_URL from environment variables
FRONTEND_URL='http://192.168.10.13:8000'

# Correct ALLOWED_HOSTS configuration after FRONTEND_URL is defined
ALLOWED_HOSTS = ['localhost', '127.0.0.1',  '0.0.0.0','192.168.10.7','192.168.10.22','192.168.10.13']

# Other settings...
CORS_ALLOW_CREDENTIALS = True
CORS_ALLOW_ALL_ORIGINS = True  # Allow all origins
CSRF_COOKIE_SECURE = False

# Use FRONTEND_URL for CORS_ALLOWED_ORIGINS
CORS_ALLOWED_ORIGINS = [f"{FRONTEND_URL}"]

# SECRET_KEY and other settings
SECRET_KEY = env("SECRET_KEY")
DEBUG = env("DEBUG", default=False)

# Application definition
INSTALLED_APPS = [
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'rest_framework_simplejwt',
    'rest_framework',
  #  'django.contrib.sites',
    'allauth',
    'allauth.account',
    'rest_framework.authtoken',  # Required for dj-rest-auth
    'allauth.socialaccount',
    'dj_rest_auth',  # Correct module for authentication
    'dj_rest_auth.registration',  # Correct module for registration
    'corsheaders', 
    'authapp',
]

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
    'corsheaders.middleware.CorsMiddleware',  # Add this
    'allauth.account.middleware.AccountMiddleware',

]

PASSWORD_HASHERS = [
    'django.contrib.auth.hashers.PBKDF2PasswordHasher',  # Default hasher
    'django.contrib.auth.hashers.Argon2PasswordHasher',  # Recommended for security
    'django.contrib.auth.hashers.BCryptSHA256PasswordHasher',
    'django.contrib.auth.hashers.BCryptPasswordHasher',
]



ROOT_URLCONF = 'trackinapp.urls'
#GOOGLE_CLIENT_ID = '6515816523-megm5vttmleeff3jgq1upaj98pd40sue.apps.googleusercontent.com'

REST_FRAMEWORK = {
    'DEFAULT_AUTHENTICATION_CLASSES': [
        'rest_framework_simplejwt.authentication.JWTAuthentication',
    ],
    'DEFAULT_PERMISSION_CLASSES': [
        'rest_framework.permissions.IsAuthenticated',
    ],
}


TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [BASE_DIR / 'authapp/templates'],        
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'trackinapp.wsgi.application'


# Database
# https://docs.djangoproject.com/en/5.1/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        'NAME': 'family_locator_auth', 
        'USER': 'postgres',
        'PASSWORD': '123',
        'HOST': '127.0.0.1', 
        'PORT': '5432',
    }
}


# Password validation
# https://docs.djangoproject.com/en/5.1/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [
    {
        'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
    },
]


# Internationalization
# https://docs.djangoproject.com/en/5.1/topics/i18n/

LANGUAGE_CODE = 'en-us'
TIME_ZONE = 'UTC'
USE_I18N = True
USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/5.1/howto/static-files/

STATIC_URL = 'static/'
# rest auth settings
# REST_AUTH = {
#     'USE_JWT': True,
#     'JWT_AUTH_COOKIE': 'access',
#     'JWT_AUTH_REFRESH_COOKIE': 'refresh',
#     'JWT_AUTH_HTTPONLY': True,
#     'SESSION_LOGIN': False,
#     'OLD_PASSWORD_FIELD_ENABLED': True,
# }

# Default primary key field type
DEFAULT_AUTO_FIELD = "django.db.models.BigAutoField"

# allauth settings
# SITE_ID = 1
# ACCOUNT_USER_MODEL_USERNAME_FIELD = None
# ACCOUNT_USERNAME_REQUIRED = False
# ACCOUNT_EMAIL_REQUIRED = True
# ACCOUNT_AUTHENTICATION_METHOD = 'email'
# ACCOUNT_EMAIL_VERIFICATION = 'mandatory'



# Default primary key field type
# https://docs.djangoproject.com/en/5.1/ref/settings/#default-auto-field

DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'

EMAIL_BACKEND = 'django.core.mail.backends.smtp.EmailBackend'
EMAIL_HOST = 'smtp.gmail.com'
EMAIL_PORT = 587
EMAIL_USE_TLS = True
EMAIL_HOST_USER = env("EMAIL_HOST_USER")
EMAIL_HOST_PASSWORD = env("EMAIL_HOST_PASSWORD")

SIMPLE_JWT = {
   'ACCESS_TOKEN_LIFETIME': timedelta(days=365),
   'REFRESH_TOKEN_LIFETIME': timedelta(days=365),
}


# settings.py
AUTH_USER_MODEL = 'authapp.CustomUser'


# import os
# os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "trackinapp/firebase_admin.json"


