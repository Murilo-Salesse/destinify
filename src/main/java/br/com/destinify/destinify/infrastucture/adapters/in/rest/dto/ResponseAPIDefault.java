package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto;

public record ResponseAPIDefault<T>(String message,
                                    T data){ }
