{{/*
Expand the name of the chart.
*/}}
{{- define "awg-storemaster.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "awg-storemaster.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "awg-storemaster.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "awg-storemaster.labels" -}}
helm.sh/chart: {{ include "awg-storemaster.chart" . }}
{{ include "awg-storemaster.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "awg-storemaster.selectorLabels" -}}
app.kubernetes.io/name: {{ include "awg-storemaster.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create a default fully qualified component name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
Usage:
{{ include "awg-storemaster.component.fullname" (dict "componentName" "component-name" "context" $) }}
*/}}
{{- define "awg-storemaster.component.fullname" -}}
{{- if .context.Values.fullnameOverride }}
{{- printf "%s-%s" .context.Values.fullnameOverride .componentName | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .context.Chart.Name .context.Values.nameOverride }}
{{- if contains $name .context.Release.Name }}
{{- printf "%s-%s" .context.Release.Name .componentName | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s-%s" .context.Release.Name $name .componentName | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Metadata labels for chart component
Usage:
{{ include "awg-storemaster.component.labels" (dict "componentName" "component-name" "context" $) }}
*/}}
{{- define "awg-storemaster.component.labels" -}}
helm.sh/chart: {{ include "awg-storemaster.chart" .context }}
{{ include "awg-storemaster.component.selectorLabels" (dict "componentName" .componentName "context" .context) }}
{{- if .context.Chart.AppVersion }}
app.kubernetes.io/version: {{ .context.Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .context.Release.Service }}
{{- end }}

{{/*
Selector labels for chart component
Usage:
{{ include "awg-storemaster.component.selectorLabels" (dict "componentName" "component-name" "context" $) }}
*/}}
{{- define "awg-storemaster.component.selectorLabels" -}}
{{ include "awg-storemaster.selectorLabels" .context }}
app.kubernetes.io/component: {{ .componentName }}
{{- end }}


{{/*
Deployment template of a Spring Boot application.
*/}}
{{- define "awg-storemaster.component.spring-deployment" -}}
{{- $r := (dict "componentName" .componentName "context" .context) -}}
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "awg-storemaster.component.fullname" $r }}
  labels:
    {{- include "awg-storemaster.component.labels" $r | nindent 4 }}
    appdev.awginc.com/application: {{ include "awg-storemaster.fullname" .context }}
spec:
  replicas: {{ .context.Values.replicaCount }}
  selector:
    matchLabels:
      {{- include "awg-storemaster.component.selectorLabels" $r | nindent 6 }}
  template:
    metadata:
      {{- with .context.Values.podAnnotations }}
      annotations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      labels:
        {{- include "awg-storemaster.component.labels" $r | nindent 8 }}
        {{- with .context.Values.podLabels }}
        {{- toYaml . | nindent 8 }}
        {{- end }}
    spec:
      {{- with .context.Values.imagePullSecrets }}
      imagePullSecrets:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      serviceAccountName: {{ include "awg-storemaster.fullname" .context }}
      {{- with .context.Values.podSecurityContext }}
      securityContext:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      containers:
        - name: java
          {{- with .context.Values.securityContext }}
          securityContext:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          image: "{{ .context.Values.image.repository }}/{{ .componentName }}:{{ .context.Values.image.tag | default .context.Chart.AppVersion }}"
          imagePullPolicy: {{ .context.Values.image.pullPolicy }}
          env:
            - name: KUBERNETES_NAMESPACE
              value: {{ .context.Release.Namespace }}
            - name: KUBERNETES_NAME
              value: {{ include "awg-storemaster.component.fullname" $r }}
            - name: SPRING_PROFILES_INCLUDE
              value: kubernetes
            - name: CONF_SERVICEBUS_ENDPOINT
              valueFrom:
                secretKeyRef:
                  name: {{ include "awg-storemaster.fullname" .context }}-servicebus
                  key: endpoint
          ports:
            - name: http
              containerPort: 8080
              protocol: TCP
          readinessProbe:
            httpGet:
              port: 8080
              path: "/actuator/health/readiness"
          livenessProbe:
            httpGet:
              port: 8080
              path: "/actuator/health/liveness"
      {{- with .context.Values.nodeSelector }}
      nodeSelector:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .context.Values.affinity }}
      affinity:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .context.Values.tolerations }}
      tolerations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
{{- end }}

{{/*
ConfigMap template of a Spring Boot application.
*/}}
{{- define "awg-v.component.spring-configmap" -}}
{{- $r := (dict "componentName" .componentName "context" .context) -}}
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ include "awg-storemaster.component.fullname" $r }}
  labels:
    {{- include "awg-storemaster.component.labels" $r | nindent 4 }}
data:
---
{{- end }}


{{/*
Service template of a Spring Boot application.
*/}}
{{- define "awg-storemaster.component.service" -}}
{{- $r := (dict "componentName" .componentName "context" .context) -}}
apiVersion: v1
kind: Service
metadata:
  name: {{ include "awg-storemaster.component.fullname" $r }}
  labels:
    {{- include "awg-storemaster.component.labels" $r | nindent 4 }}
spec:
  type: ClusterIP
  ports:
    - name: http
      protocol: TCP
      port: 80
      targetPort: http
  selector:
    {{- include "awg-storemaster.component.selectorLabels" $r | nindent 4 }}
---
{{- end }}
