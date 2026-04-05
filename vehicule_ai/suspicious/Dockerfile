# Use official Python image
FROM python:3.11-slim

# Set working directory
WORKDIR /app

# Copy requirements first (better caching)
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy project files
COPY . .

# Expose port
EXPOSE 8050

# Run FastAPI app
CMD ["uvicorn", "suspicious:app", "--host", "0.0.0.0", "--port", "8050"]